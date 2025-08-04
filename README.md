# EyePatch

An (eye) patch to your repositories!

## *But seriously, what is it?*

EyePatch is a gradle plugin similar to [gitpatcher](https://github.com/zml2008/gitpatcher),
but instead of git patches, EyePatch works on file patches.

## Usage

Configuring EyePatch is pretty easy, for example:

```kts
eyepatch.repositories {
    create("your_repository") {
        submodule = "AmazingSubmodule"
        target = file("PatchedPlace") // default: {repository_name} (your_repository)
        patches = file("patches") // default: patches/{repository_name} (patches/your_repository)
    }
}
```

Then, you can use these tasks:

- `apply{capitalized repository_name}Patches` - to apply patches
- `make{capitalized repository_name}Patches` - to make patches

Instead of `{capitalized repository_name}` you can also use `All` to work on all created repositories.

