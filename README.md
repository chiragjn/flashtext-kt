Kotlin port of [flashtext](https://github.com/vi3k6i5/flashtext/)
---

A modified version of Aho Corasick algorithm that only matches whole words instead of arbitrary substrings [1]

[1] [https://arxiv.org/abs/1711.00046](https://arxiv.org/abs/1711.00046)

---

See example.kt for usage till more documentation is added

**Example Usage:**

```kotlin
import flashtext.KeywordProcessor as KeywordProcessor

fun main(args: Array<String>) {
    val keywordProcessor = KeywordProcessor(caseSensitive=true)
    keywordProcessor.addKeyword("NYC", "New York")
    keywordProcessor.addKeyword("APPL", "Apple")
    keywordProcessor.addKeywordsFromMap(
        hashMapOf(
            ("java" to arrayListOf("java_2e", "java programing")),
            ("product manager" to arrayListOf("PM", "product manager"))
        )
    )
    println("Terms in Trie: ${keywordProcessor.size()}")
    println("Data: ${keywordProcessor.getAllKeywords().toString()}")

    val text: String = "I am a PM for a java_2e platform working from APPL, NYC"
    println("Text: ${text}")
    println("Extract: ${keywordProcessor.extractKeywords(text)}")
    println("Replace: ${keywordProcessor.replaceKeywords(text)}")
}
```

**Compile:**

```
kotlinc flashtext/TrieNode.kt flashtext/KeywordProcessor.kt example.kt -d flashtext.jar
```

**Run the example:**

```
kotlin -classpath flashtext.jar ExampleKt
```

**Output**

```
Terms in Trie: 6
Data: {APPL=Apple, java_2e=java, product manager=product manager, NYC=New York, java programing=java, PM=product manager}
Text: I am a PM for a java_2e platform working from APPL, NYC
Extract: [(value=product manager, offset=7, length=2), (value=java, offset=16, length=7), (value=Apple, offset=46, length=4), (value=New York, offset=52, length=3)]
Replace: I am a product manager for a java platform working from Apple, New York
```
---

**Todo:**

- [ ] Make it into a proper package, probably usable via gradle. Get help for this
- [ ] Write tests
- [ ] Compute benchmarks for Kotlin Regex vs this module
- [ ] Profile memory for a bunch of dictionaries

----

**Disclaimer:** I am a Kotlin newbie, so any idiomatic Kotlin changes are welcome.

