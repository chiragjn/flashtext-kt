package flashtext

class TrieNode() {
    var children: HashMap<Char, TrieNode> = hashMapOf()
    var value: String? = null
    var isEndNode: Boolean = false
    
    val data: HashMap<String, String>
    get() = this.toFlatMap()

    fun contains(char: Char): Boolean {
        return this.children.contains(char)
    }

    fun get(char: Char): TrieNode? {
        if (this.contains(char)) {
            return this.children[char]
        }
        return null
    }

    private fun searchKey(key: String): Pair<Boolean, String?> {
        var sr: Pair<Boolean, String?> = Pair(false, null)
        var currentNode: TrieNode = this
        var lengthCovered: Int = 0
        for (char in key) {
            if(currentNode.contains(char)) {
                currentNode = currentNode.get(char)!!
                lengthCovered += 1
            }
            else {
                break
            }
        }
        if(currentNode.isEndNode && lengthCovered == key.length) {
            sr = Pair(true, currentNode.value)
        }
        return sr
    }

    fun put(key: String, value: String): Boolean {
        var currentNode: TrieNode = this
        for(char in key) {
            if (!currentNode.contains(char)) {
                currentNode.children[char] = TrieNode()
            }
            currentNode = currentNode.get(char)!!
        }
        if (currentNode.value == null) {
            currentNode.value = value
            currentNode.isEndNode = true
            return true
        }
        return false
    }

    fun delete(key: String): Boolean {
        var charsToMaps: ArrayList<Pair<Char, TrieNode>> = ArrayList()
        var found: Boolean = true
        var currentNode: TrieNode = this
        
        for (char in key) {
            if (currentNode.contains(char)) {
                charsToMaps.add(Pair(char, currentNode)) // Will this work or do we need to copy reference ?
                currentNode = currentNode.get(char)!!
            }
            else {
                found = false
                break
            }
        }

        if (found && currentNode.isEndNode) {
            for((char, trieNode) in charsToMaps.reversed()) {
                if (trieNode.children.size == 1) {
                    trieNode.children.remove(char)
                }
                else {
                    trieNode.children.remove(char)
                    break
                }
            }
            return true
        }

        return false
    }

    fun getValue(key: String): String? {
        return this.searchKey(key).second
    }

    fun containsKey(key: String): Boolean {   
        return this.searchKey(key).first    
    }

    

    fun toFlatMap(): HashMap<String, String> {
        var map: HashMap<String, String> = hashMapOf()
        
        if (this.isEndNode && this.value != null) {
            map[""] = this.value!!
        }
     
        for((char, trieNode) in this.children) {
            val submap: HashMap<String, String> = trieNode.toFlatMap()
            for ((subKey, value) in submap) {
                map[char + subKey] = value
            }
        }

        return map
    }
}
