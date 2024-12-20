package io.github.faiyaz26.detekt.inclusivelanguage.rules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtPackageDirective
import org.jetbrains.kotlin.psi.KtVariableDeclaration

/**
 * Custom Detekt rule to detect non-inclusive language in code.
 * Checks identifiers and string literals for problematic terms and suggests alternatives.
 */
internal class InclusiveLanguage(config: Config) : Rule(config) {
    override val issue = Issue(
        id = "InclusiveLanguage",
        severity = Severity.Warning,
        description = "Detects non-inclusive language and suggests inclusive alternatives",
        debt = Debt.FIVE_MINS,
    )

    private val termMappings = mapOf(
        "whitelist" to "allowlist",
        "blacklist" to "denylist",
        "master" to "main",
        "slave" to "replica/secondary",
        "blackbox" to "opaquebox/closedbox",
        "whitebox" to "transparentbox",
        "grandfathered" to "legacy",
        "grandfather" to "legacy",
        "sanity check" to "confidence check",
        "dummy" to "placeholder/mock",
    )

    override fun visitElement(element: PsiElement) {
        super.visitElement(element)

        when {
            // Check identifiers (variable names, function names, etc.)
            element is KtClassOrObject || element is KtNamedFunction || element is KtVariableDeclaration || element is KtPackageDirective -> {
                if (element.text.isNotBlank()) {
                    checkText(element.text, element)
                }
            }
        }
    }

    private fun reportCodeSmell(element: PsiElement,
                                offendingText: String,
                                replacement: String) {
        report(
            CodeSmell(
                issue,
                Entity.from(element),
                message = "Non-inclusive term '$offendingText' found. " +
                        "Consider using '$replacement' instead.",
            ),
        )
    }

    private fun checkText(text: String, element: PsiElement) {
        val problematicWords = termMappings.keys.map { it.lowercase() }
        for (problematicWord in problematicWords) {
            if (text.contains(problematicWord, ignoreCase = true)) {
                reportCodeSmell(element, problematicWord, termMappings[problematicWord]!!)
                break
            }
        }
    }

    companion object {
        const val RULE_ID = "InclusiveLanguage"
    }
}
