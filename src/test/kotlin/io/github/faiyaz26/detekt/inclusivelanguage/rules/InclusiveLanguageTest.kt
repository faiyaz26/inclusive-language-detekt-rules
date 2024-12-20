package io.github.faiyaz26.detekt.inclusivelanguage.rules

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.test.compileAndLint
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class InclusiveLanguageTest {
    private val rule = InclusiveLanguage(Config.empty)

    @Test
    fun `reports non-inclusive language in variable names`() {
        val code = """
            interface BlackBox {
                fun whiteBox()
            }
            val whitelist = listOf("allowed")
            val blacklist = listOf("denied")
            val whitelistedBox = "opaque"
            class Master {
                fun slave() {}
            }
        """.trimIndent()

        val findings = rule.compileAndLint(code)
        assertEquals(7, findings.size)
    }

    @Test
    fun `does not report inclusive language`() {
        val code = """
            val allowlist = listOf("allowed")
            val denylist = listOf("denied")
        """.trimIndent()

        val findings = rule.compileAndLint(code)
        assertEquals(0, findings.size)
    }
}
