# [MASTG-TEST-0004: Determining Whether Sensitive Data Is Shared with Third Parties via Embedded Services](https://mas.owasp.org/MASTG/tests/android/MASVS-STORAGE/MASTG-TEST-0004)

## Overview
MASVS-STORAGE-2 / MSTG-STORAGE-4 / May 08, 2023
## Static Analysis

To determine whether API calls and functions provided by the third-party library are used according to best practices, review their source code, requested permissions and check for any known vulnerabilities.

All data that's sent to third-party services should be anonymized to prevent exposure of PII (Personal Identifiable Information) that would allow the third party to identify the user account. No other data (such as IDs that can be mapped to a user account or session) should be sent to a third party.