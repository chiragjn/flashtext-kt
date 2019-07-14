package flashtext
import flashtext.TrieNode as TrieNode

data class Match(val value: String, val offset: Int, val length: Int) {
    override fun toString(): String {
        return "(value=${this.value}, offset=${this.offset}, length=${this.length})"
    }
}

class KeywordProcessor(val caseSensitive: Boolean = false) {
    // private val whiteSpaceChars: HashSet<Char> = hashSetOf('.', '\t', '\n', ' ', ',') // '\a' is not supported ?
    
    private var nonWordBoundaries: HashSet<Char> = (('0'..'9') + ('A'..'Z') + ('a'..'z') + ('_')).toHashSet()
    get() = field
    set(value) {
        field = value
    }

    private var keywordTrieDict: TrieNode = TrieNode()
    
    private var termsInTrie: Int = 0

    private fun processText(_text: String): String {
        var text: String = _text
        if (!this.caseSensitive) {
            text = text.toLowerCase()
        }
        return text
    }

    fun size(): Int {
        return this.termsInTrie
    }

    fun contains(_word: String): Boolean {
        var word = this.processText(_word)
        return this.keywordTrieDict.containsKey(word)
    }
    
    fun add_non_word_boundary(character: Char) {
        this.nonWordBoundaries.add(character)
    }

    fun getKeyword(_word: String): String? {
        var word = this.processText(_word)
        return this.keywordTrieDict.getValue(word)
    }

    fun addKeyword(_word: String, _cleanName: String? = null): Boolean {
        var status: Boolean = false
        var word: String = _word
        var cleanName: String = word
        
        if (_cleanName != null) {
            cleanName = _cleanName
        }

        if (word.length > 0 && cleanName.length > 0) {
            word = this.processText(word)
            status = this.keywordTrieDict.put(word, cleanName) 
            if (status) {
                this.termsInTrie += 1
            }
        }

        return status
    }

    fun removeKeyword(_word: String): Boolean {
        val word: String = this.processText(_word)
        val status: Boolean = this.keywordTrieDict.delete(word)
        if (status) {
            this.termsInTrie -= 1
        }
        return status
    }

    fun getAllKeywords(): HashMap<String, String> {
        return this.keywordTrieDict.data
    }

    fun extractKeywords(_sentence: String): ArrayList<Match> {
        var matches: ArrayList<Match> = ArrayList()
        
        if (_sentence.length == 0) {
            return matches
        }

        val sentence: String = this.processText(_sentence)
        val sentenceLength: Int = sentence.length
        var currentNode: TrieNode = this.keywordTrieDict
        var resetCurrentNode: Boolean = false
        var index: Int = 0
        var start: Int = 0
        var end: Int = -1
        
        while (index < sentenceLength) {
            var char: Char = sentence[index]
            if (!this.nonWordBoundaries.contains(char)) {
                // we reached a word boundary \b
                if (currentNode.isEndNode || currentNode.contains(char)) {
                    // var matchValue: String? = null
                    var longestMatchValue: String? = null
                    var longerMatchFound: Boolean = false
                    
                    if (currentNode.isEndNode) {
                        // matchValue = currentNode.value!!
                        longestMatchValue = currentNode.value!!
                        end = index
                    }

                    if (currentNode.contains(char)) {
                        var tempCurrentNode: TrieNode = currentNode.get(char)!!
                        var j: Int = index + 1
                        
                        while(j < sentenceLength) {
                            val innerChar = sentence[j]
                            if (!this.nonWordBoundaries.contains(innerChar) && tempCurrentNode.isEndNode) {
                                longestMatchValue = tempCurrentNode.value!!
                                end = j
                                longerMatchFound = true
                            }

                            if (tempCurrentNode.contains(innerChar)) {
                                tempCurrentNode = tempCurrentNode.get(innerChar)!!
                            }
                            else {
                                break
                            }

                            j += 1
                        }

                        if (j >= sentenceLength) {
                            // we reached end of sentence
                            if (tempCurrentNode.isEndNode) {
                                longestMatchValue = tempCurrentNode.value!!
                                end = j
                                longerMatchFound = true
                            }
                        }

                        if (longerMatchFound) {
                            index = end
                        }
                    }

                    currentNode = this.keywordTrieDict
                    
                    if (longestMatchValue != null) {
                        matches.add(Match(longestMatchValue, start, end - start))
                    }
                    resetCurrentNode = true
                }
                else {
                    // no match, go back to trie root
                    currentNode = this.keywordTrieDict
                    resetCurrentNode = true
                }
            }
            else if (currentNode.contains(char)) {
                // keep going
                currentNode = currentNode.get(char)!!
            }
            else {
                // Our trie does not contain current word, so skip and reset
                currentNode = this.keywordTrieDict
                resetCurrentNode = true
                var j: Int = index + 1
                
                while(j < sentenceLength) {
                    char = sentence[j]
                    if (!this.nonWordBoundaries.contains(char)) {
                        break
                    }
                    j += 1
                }
                index = j
            }
            
            if (index + 1 >= sentenceLength) {
                if (currentNode.isEndNode) {
                    matches.add(Match(currentNode.value!!, start, sentenceLength - start))
                }
            }
            
            index += 1

            if (resetCurrentNode) {
                resetCurrentNode = false
                start = index
            }
        }
        
        return matches
    }

    fun replaceKeywords(sentence: String): String {
        var stringBuilder: StringBuilder = StringBuilder()
        var i: Int = 0
        val sentenceLength: Int = sentence.length
        
        for (match in this.extractKeywords(sentence)) {
            while (i < match.offset) {
                stringBuilder.append(sentence[i])
                i += 1
            }
            stringBuilder.append(match.value)
            i += match.length
        }
        
        while (i < sentenceLength) {
            stringBuilder.append(sentence[i])
            i += 1
        }

        return stringBuilder.toString()
    }

    fun addKeywordsFromMap(map: Map<String, List<String>>) {
        // cleanName to List of keywords
        for ((value, variants) in map) {
            for (variant in variants) {
                this.addKeyword(variant, value)
            }
        }
    }

    fun addKeywordsFromList(values: List<String>) {
        for (value in values) {
            this.addKeyword(value)
        }
    }
}