# [MASTG-TEST-0040: Testing for Debugging Symbols](https://mas.owasp.org/MASTG/tests/android/MASVS-RESILIENCE/MASTG-TEST-0040)

## Implementation

```java
#PATH NDK
/Users/lucaferrari/Library/Android/sdk/ndk/27.0.11718014/toolchains/llvm/prebuilt/darwin-x86_64/bin/
```

La guida Owasp usa questo comando:

`export NM = $ANDROID_NDK_DIR/toolchains/arm-linux-androideabi-4.9/prebuilt/darwin-x86_64/bin/arm-linux-androideabi-nm`

ma `arm-linux-androideabi-nm` non esiste, di conseguenza non posso usare i seguenti comandi:

```java
$NM -a libfoo.so
$NM -D libfoo.so
```

alternativamente →Usare ghidra

set debugging symbols: https://stackoverflow.com/questions/7990844/creating-symbol-table-for-gdb-using-cmake

aggiunto in CMake.txt

`set(CMAKE_BUILD_TYPE Debug)`

bisogna essere sicuri che nel gradle ci sia :

```java
externalNativeBuild {
    cmake {
        cppFlags "-fvisibility=hidden"
    }
}
```

nell’app vulnerable non è stato inserito.

creato app uguale a `MASTG-TEST0044`

## Overview
MASVS-RESILIENCE-3 / MSTG-CODE-3 / May 08, 2023
## Static Analysis
Symbols are usually stripped during the build process, so you need the compiled bytecode and libraries to make sure that unnecessary metadata has been discarded.

First, find the `nm` binary in your Android NDK and export it (or create an alias).

```
export NM = $ANDROID_NDK_DIR/toolchains/arm-linux-androideabi-4.9/prebuilt/darwin-x86_64/bin/arm-linux-androideabi-nm
```

To display debug symbols:

```
$NM -a libfoo.so
/tmp/toolchains/arm-linux-androideabi-4.9/prebuilt/darwin-x86_64/bin/arm-linux-androideabi-nm: libfoo.so: no symbols
```

To display dynamic symbols:

```
$NM -D libfoo.so
```

Alternatively, open the file in your favorite disassembler and check the symbol tables manually.

Dynamic symbols can be stripped via the `visibility` compiler flag. Adding this flag causes gcc to discard the function names while preserving the names of functions declared as `JNIEXPORT`.

Make sure that the following has been added to build.gradle:

```
externalNativeBuild {
    cmake {
        cppFlags "-fvisibility=hidden"
    }
}
```