package com.faiyaz26.detekt.inclusivelanguage

import com.faiyaz26.detekt.inclusivelanguage.rules.InclusiveLanguage
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

class InclusiveLanguageProvider : RuleSetProvider {
    override val ruleSetId: String = "InclusiveLanguageRuleSet"

    override fun instance(config: Config): RuleSet = RuleSet(
        ruleSetId,
        listOf(InclusiveLanguage(config)),
    )
}
