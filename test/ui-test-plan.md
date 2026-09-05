# UI Test Plan

This plan records deterministic console UI tests for Dr. Pijon. Each test case runs in a
fresh process so that task state cannot leak between cases.

## Test runner

- Runtime: Java 25.
- Working directory: repository root.
- Prepare the program with the project's normal Java 25 compile workflow.
- Example command after compiling classes to `build/classes`:
  `python .codex/skills/test-ui/scripts/run_ui_tests.py --plan test/ui-test-plan.md --program "java -cp build/classes drpijon.Main"`
- Input comparison: the runner joins each case's `inputs` with newlines and adds a final newline.
- Output comparison: stdout and stderr are captured as one console stream and compared exactly,
  after normalizing only `CRLF` versus `LF` line endings.
- Failure policy: print the complete console input and output, report actual versus expected
  output, and stop immediately at the first failed case.

## Test cases

Add each case with a unique ID, a concise aim, an ordered list of console inputs, and exactly
one expected-output field. `expected_output_lines` is convenient for line-oriented output and
expects a final newline; `expected_output` is available for an exact multiline string.

### Executable test cases

The executable list is empty until concrete UI scenarios are supplied.

```json
[]
```

After a test run, append the runner's console session record below the relevant case and mark
its result as `PASS` or `FAIL`. Do not remove a failed case's actual output.
