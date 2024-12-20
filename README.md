# Inclusive Language Detekt Rule

This rule is created to encourage developers to 
use inclusive language in their code.

Such as refraining from using words like "master" 
or "blacklist".

## Usage
In the `dependencies` block of your `build.gradle` file, add the following:
```groovy
detektPlugins("io.github.faiyaz26:inclusive-language-detekt-rules:0.0.5")
```
or if you're using a `libs.version.toml` file, add this there:
```
inclusive-language-detekt-rules = { module = "io.github.faiyaz26:inclusive-language-detekt-rules", version = "0.0.1" }
```
and this in your `build.gradle` file:
```groovy
detektPlugins(rootProject.libs.inclusive-language-detekt-rules)
```

## Configuration
The below block is a starting point for the configuration to add to `detekt.yml`, and can be modified as needed:
```yaml
InclusiveLanguageRuleSet:
  active: true
  InclusiveLanguage:
    active: true
    shouldReportString: false
    skipWords: []
  excludes: ["**Test.kt"]
```

# Releasing

To release a new version:

1. Create a PR that bumps the `version` in [`build.gradle.kts`](./build.gradle.kts).
2. Once this PR merges with the new version and completes CI, create a new version through Github releases