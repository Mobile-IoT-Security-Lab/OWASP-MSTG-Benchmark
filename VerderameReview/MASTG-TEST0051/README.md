# [MASTG-TEST-0051: Testing Obfuscation](https://mas.owasp.org/MASTG/tests/android/MASVS-RESILIENCE/MASTG-TEST-0051)

## Nota

non ho capito la logica dell'app (è necessaria?)

## Implementation

- Creata applicazione che permette di compare caffè ma il credit è zero.
- per comprare un caffè bisogna inserire il promo code, harcodato nel codice.

## Overview
MASVS-RESILIENCE-3 / MSTG-RESILIENCE-9 / May 13, 2024
## Static Analysis
Decompile the APK and review it to determine whether the codebase has been obfuscated.

Below you can find a sample for an obfuscated code block:

```
package com.a.a.a;

import com.a.a.b.a;
import java.util.List;

class a$b
  extends a
{
  public a$b(List paramList)
  {
    super(paramList);
  }

  public boolean areAllItemsEnabled()
  {
    return true;
  }

  public boolean isEnabled(int paramInt)
  {
    return true;
  }
}
```
Here are some considerations:

- Meaningful identifiers, such as class names, method names, and variable names, might have been discarded.
- String resources and strings in binaries might have been encrypted.
- Code and data related to the protected functionality might be encrypted, packed, or otherwise concealed.

For native code:

- [libc APIs](https://en.wikipedia.org/wiki/C_standard_library#Standard_libraries) (e.g open, read) might have been replaced with OS [syscalls](https://en.wikipedia.org/wiki/System_call).
- [Obfuscator-LLVM](https://github.com/obfuscator-llvm/obfuscator) might have been applied to perform [Control Flow Flattening](https://en.wikipedia.org/wiki/Control-flow_integrity) or [Bogus Control Flow](https://github.com/obfuscator-llvm/obfuscator).

Some of these techniques are discussed and analyzed in the blog post ["Security hardening of Android native code"](https://www.nowsecure.com/blog/2017/10/31/security-hardening-android-native-code/) by Gautam Arvind and in the ["APKiD: Fast Identification of AppShielding Products"](https://www.blackhat.com/docs/us-17/wednesday/us-17-Novella-APKiD-Fast-Identification-Of-AppShielding-Products-wp.pdf) presentation by Eduardo Novella.

For a more detailed assessment, you need a detailed understanding of the relevant threats and the obfuscation methods used. Tools such as [APKiD](https://github.com/rednaga/APKiD) may give you additional indications about which techniques were used for the target app such as obfuscators, packers, and anti-debug measures.
