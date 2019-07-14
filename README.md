Kotlin port of [flashtext](https://github.com/vi3k6i5/flashtext/)
---

A modified version of Aho Corasick algorithm that only matches whole words instead of arbitrary substrings [1]

[1] [https://arxiv.org/abs/1711.00046](https://arxiv.org/abs/1711.00046)

---

**Compile:**

```
kotlinc flashtext/TrieNode.kt flashtext/KeywordProcessor.kt example.kt -d flashtext.jar
```

**Run the example:**

```
kotlin -classpath flashtext.jar ExampleKt
```

---

**Todo:**

[ ] - Make it into a proper package, probably usable via gradle. Get help for this
[ ] - Write tests
[ ] - Compute benchmarks for Kotlin Regex vs this module
[ ] - Profile memory for a bunch of dictionaries

----

**Disclaimer:** I am a Kotlin newbie, so any idiomatic Kotlin changes are welcome.

