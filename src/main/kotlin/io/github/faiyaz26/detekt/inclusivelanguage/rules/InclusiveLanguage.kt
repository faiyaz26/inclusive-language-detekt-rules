package io.github.faiyaz26.detekt.inclusivelanguage.rules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import io.gitlab.arturbosch.detekt.api.config
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtPackageDirective
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtStringTemplateExpression

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

    private val shouldReportString = valueOrDefault("shouldReportString", true)
    private val skipWords = valueOrDefault("skipWords", emptyList<String>()).toSet()

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

    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)
        if (function.name?.isNotBlank() == true) {
            checkText(function, function.name!!)
        }
    }

    override fun visitClassOrObject(classOrObject: KtClassOrObject) {
        super.visitClassOrObject(classOrObject)
        if (classOrObject.name?.isNotBlank() == true) {
            checkText(classOrObject, classOrObject.name!!)
        }
    }

    override fun visitProperty(property: KtProperty) {
        super.visitProperty(property)
        if (property.name?.isNotBlank() == true) {
            checkText(property, property.name!!)
        }
    }

    override fun visitPackageDirective(directive: KtPackageDirective) {
        super.visitPackageDirective(directive)
        if (directive.text.isNotBlank()) {
            checkText(directive, directive.text)
        }
    }

    override fun visitStringTemplateExpression(expression: KtStringTemplateExpression) {
        super.visitStringTemplateExpression(expression)
        if (shouldReportString && expression.text.isNotBlank()) {
            checkText(expression, expression.text)
        }
    }

    private fun reportCodeSmell(element: PsiElement, offendingText: String, replacement: String) {
        report(
            CodeSmell(
                issue,
                Entity.from(element),
                message = "'$offendingText' contains non inclusive term. " +
                    "Consider using '$replacement' instead.",
            ),
        )
    }

    private fun checkText(element: PsiElement, text: String = element.text) {
        val problematicWords = termMappings.keys.map { it.lowercase() }

        if (skipWords.contains(text)) {
            return
        }

        for (problematicWord in problematicWords) {
            if (text.contains(problematicWord, ignoreCase = true)) {
                reportCodeSmell(element, text, termMappings[problematicWord]!!)
                break
            }
        }
    }

    companion object {
        const val RULE_ID = "InclusiveLanguage"
    }
}
