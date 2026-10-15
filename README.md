# cs2114-project2-group84
# The Pass — Project 2 Prototype

**Team:** Abigail, Bryan, Jack, and Talal

The Pass is a single-user console application for managing a restaurant’s food and drink orders. Staff can add an item for a table, inspect pending queues, complete the oldest entry, cancel a selected entry, and view completed orders.

Each entry receives a unique ID, so repeated orders for the same menu item remain separate. Data stays in memory for one run.

## Working Application Slice

The prototype implements the console MVP described in our specification.

| Command | Action |
| --- | --- |
| 1 | Add food to the kitchen queue |
| 2 | Add a drink to the bar queue |
| 3 | View the kitchen queue |
| 4 | View the bar queue |
| 5 | Complete the oldest food entry |
| 6 | Complete the oldest drink entry |
| 7 | Cancel a food entry by its displayed position |
| 8 | Cancel a drink entry by its displayed position |
| 9 | View one table’s pending food and drinks |
| 10 | View the shared sent-out log |
| 0 | Exit |

The fixed food menu contains **burger, fries, and salad**. The drink menu contains **water, coffee, and lemonade**.

Item names are trimmed and normalized using `Locale.ROOT`. Pending views show the order’s position, ID, table, item, and elapsed whole minutes. Completed entries move into the sent-out log in completion order. Canceled entries are removed without being logged.

### Generic LinkedChain Reuse

OrderManager uses three instances of the same generic LinkedChain class:

```java
private final LinkedChain<FoodOrder> kitchen;
private final LinkedChain<DrinkOrder> bar;
private final LinkedChain<Order> sentOut;
```

FoodOrder and DrinkOrder are two different element types. Both inherit shared order data from Order. The shared `LinkedChain<Order>` log accepts either subtype.

The application uses the chain through its public methods, including:

- `addToEnd`
- `remove`
- `get`
- `size`
- `isEmpty`
- `toArray`

Application classes never access chain nodes or internal fields. Menu rules, validation, formatting, and order operations remain in the application classes. No separate food-specific or drink-specific chain implementation is used.

## Running the Application

Use Java 8 or later.

From the repository root, compile and run the application:

```bash
mkdir -p out
javac -d out src/*.java
java -cp out ConsoleApp
```

If JUnit test files are also stored directly in `src`, include JUnit and its dependencies on the compilation classpath, or use Eclipse’s configured project build.

### Running in Eclipse

1. Import the `src` folder as an existing Eclipse project. This folder contains the project configuration for **ThePass**.
2. Open ConsoleApp.java.
3. Right-click the file and select **Run As → Java Application**.
4. Enter command numbers in the Console pane.

To run a test class, right-click it and select **Run As → JUnit Test**. The tests drafted for this project use JUnit 4.

## Tests and Validation

**Test status: the team reports that all test classes are completed and passing.** The current test classes will be committed with this README.

Our test plan requires at least one normal case and one bad-input or boundary case for each public application method. Methods without input parameters use meaningful boundary cases, such as empty queues or repeated display calls.

| Test class | Test coverage |
| --- | --- |
| FoodOrderTest.java | Valid constructor fields; invalid IDs, tables, item names, and creation times |
| DrinkOrderTest.java | Valid and invalid constructor cases for drink orders |
| OrderTest.java | Getters for both order types, elapsed-minute calculations, invalid times, equality, and hash codes |
| MenuTest.java | Fixed menu categories, normalization, null and blank input, unknown items, wrong categories, and display output |
| OrderManagerTest.java | Addition, completion, cancellation, views, FIFO behavior, duplicate items, unique IDs, invalid inputs, and unchanged state after rejected operations |
| ConsoleAppTest.java | Valid commands, invalid commands, malformed and overflowing numbers, corrected input, empty completion, cancellation errors, and EOF handling |
| LinkedChainTest.java | The actual chain API, insertion order, removal, indexed retrieval, searches, counts, size, empty states, null behavior, and array copies |

During preparation of this README, all seven application source files compiled successfully using Java 17 with Java 8 compatibility and compiler warnings treated as errors.

Scripted console checks also passed for:

- Adding food and drink orders.
- Displaying pending orders in arrival order.
- Canceling a selected entry.
- Completing orders across both queues.
- Displaying a table’s pending orders and the completed log.
- Recovering from invalid commands, malformed numbers, and invalid menu inputs.
- Handling invalid table numbers and cancellation positions.
- Ending input during an unfinished command without adding a partial order.

These console checks are separate from the team’s reported JUnit results.

### Validation Responsibilities

| Layer | Responsibility |
| --- | --- |
| ConsoleApp | Parse numbers, detect overflow and EOF, display messages, and re-prompt after expected errors |
| OrderManager | Enforce table, menu, category, cancellation, completion, and ID rules for every caller |
| Order | Enforce valid order fields and reject a wait-time argument earlier than creation |
| LinkedChain | Enforce its storage contracts and maintain the generic linked structure |

## Reflection

### What Changed from the Specification?

The prototype retains the proposed application classes and their public method signatures. The main change was adapting the application to the staff-provided LinkedChain API instead of implementing the additional chain methods proposed in our specification.

| Specification proposal | What the prototype uses instead | Reason |
| --- | --- | --- |
| `add` appends at the tail | `addToEnd` | The supplied `add` inserts at the front. Appending preserves arrival order. |
| `removeFirst` completes the oldest entry | `isEmpty` followed by `remove` | These methods already support front removal. The manager supplies the required exception when the queue is empty. |
| `removeAt` cancels an indexed entry | `get(position - 1)` followed by `remove(order)` | This uses the existing API. Unique IDs identify the selected order even when menu items repeat. |
| `toList` creates copied views | Typed arrays returned through `toArray` | Arrays provide snapshots without exposing chain nodes. |
| A tail reference supports O(1) append | Traversal-based `addToEnd` | The current chain has a first-node reference and entry count, without a tail reference. |

These choices changed some complexity estimates:

- Adding food takes O(k + s), where k is the kitchen queue size and s is the input string length.
- Adding a drink takes O(b + s), where b is the bar queue size.
- Completing an order removes the queue front in O(1), but appending to a completed log of length l takes O(l).
- Cancellation remains O(k) or O(b), although retrieving and removing the selected entry may each traverse the chain.

Validation has two small differences from the original design:

- ConsoleApp checks that a table number is positive before collecting an item. OrderManager and Order still validate the table independently.
- Entering **0** at a cancellation prompt returns to the main menu. Calling a manager cancellation method directly with position 0 still throws IndexOutOfBoundsException.

Invalid cancellation positions re-prompt within the cancellation command.

The manager retains an `int` ID counter. After issuing Integer.MAX_VALUE, it sets the counter to 0 as an exhausted sentinel. Future additions throw IllegalStateException before changing storage. This prevents overflow and ID reuse.

ConsoleApp currently repeats the fixed menu labels instead of calling Menu.display. The labels match the menu, but centralizing this text would make future changes easier.

### What Changed in LinkedChain?

The LinkedChain.java file in the repository credits **CS 2114 Staff**. The application uses its existing generic API and contains no restaurant-specific additions to that class.

The proposed tail reference, removeFirst, removeAt, and toList extensions were not required for this implementation.

One difference from the specification concerns null searches:

- `contains(null)` returns false.
- `count(null)` returns 0.
- Null additions still throw IllegalArgumentException.

The tests should follow these actual contracts rather than expecting exceptions for null searches.

The README review did not modify LinkedChain.java. The team should confirm and document any earlier edits before describing the file as unchanged from the original starter.

### What Happened to Our Unknowns?

Several implementation questions were resolved:

- **Preserving FIFO order:** We use addToEnd because the starter’s add method inserts at the front.
- **Reusing one generic class:** Food, drink, and completed-order storage use differently typed instances of the same LinkedChain class.
- **Canceling repeated menu items:** Each order has a unique ID, and equality compares IDs.
- **Handling bad input:** Console parsing loops and expected exceptions allow staff to correct input and continue.
- **Displaying wait times:** Wait time means elapsed whole minutes rather than an estimated preparation time.

The specification identifies a usable graphical screen as an original-scope unknown. The prototype uses a console interface, while a GUI remains a stretch goal.

The team should compare this reflection with the original scope and include any additional unknowns that were listed there.

### What Does Not Work Yet?

With the team’s passing-test status assumed, no missing console MVP feature or failing JUnit case is currently reported.

The following stretch features are not implemented:

- Order customizations.
- Appetizer-before-main sequencing.
- Priority reordering.
- A graphical interface.

Persistence and simultaneous staff users remain outside the specified MVP.

The main technical limitation is traversal-based appending, which can become slower as queues and the completed log grow. Menu text is also duplicated between Menu and ConsoleApp. These limitations do not prevent the current fixed-menu console workflow from running.

## Next Steps

### What Is Left to Finish the MVP?

No additional MVP command is currently planned. Remaining work focuses on committing the completed tests, checking the complete project on the course setup, and finalizing documentation.

The assignments below are proposed and should be confirmed by the team.

| Order | Remaining work | Proposed owner |
| --- | --- | --- |
| 1 | Push the completed tests and JUnit configuration with the current source; rerun the suite from the committed version | Bryan, with each test author checking their files |
| 2 | Confirm starter provenance, storage contracts, generic reuse, and complexity descriptions | Jack |
| 3 | Centralize console menu text through Menu while preserving useful prompts; rerun affected tests | Talal |
| 4 | Run the complete console demonstration on a teammate’s setup, including invalid input and EOF handling | Abigail and Bryan |
| 5 | Confirm actual contributions, original-scope unknowns, and final stretch decisions; update this README | Abigail, with all team members |

### Which Stretch Goals Will We Attempt?

Our proposed stretch goal is **optional item customizations**, such as “no pickles,” after the integrated MVP continues to pass its tests.

Proposed responsibilities:

- **Talal:** Order data, manager methods, and console integration.
- **Bryan:** Validation and tests.
- **Jack:** Review storage independence and generic reuse.
- **Abigail:** Review displays, demonstration behavior, and documentation.

The proposed final-submission plan drops:

- GUI development.
- Appetizer-before-main sequencing.
- Priority reordering.

These features require additional interface or scheduling rules. Priority reordering would also require explicitly changing the FIFO service contract. The team should confirm these decisions before submission.

### What New Tests Will We Need?

For the menu-display cleanup:

- Correct food and drink prompts.
- Valid and wrong-category menu selections.
- Blank input and EOF recovery.

For customization constructors or methods:

- Normal notes.
- Omitted or null notes.
- Trimmed empty notes.
- The agreed maximum-length boundary and over-limit rejection.
- Unchanged queues and next ID after an invalid addition.

Integration tests should also check:

- Customizations on both food and drink orders.
- Duplicate menu items with different notes.
- Notes appearing in pending and table views.
- Notes remaining attached when an entry moves to the completed log.
- Unchanged ID equality and FIFO behavior.

Existing integration and bad-input tests will be rerun after each added feature. Every new public method will receive a normal case and a bad-input or boundary case.

## Attribution

The specification records design contributions by the four team members. These are listed below, but they do not establish who wrote, revised, reviewed, or ran each current code file.

**Before submission, complete the final column with the actual file-level contributions.** Test filenames should match the files committed with this README.

| Class or test file | Known contribution or attribution | Implementation or test contributor |
| --- | --- | --- |
| LinkedChain.java | CS 2114 Staff supplied the generic class; Jack contributed chain API and complexity design review | Confirm team edits and implementation review |
| Order.java | Talal contributed class/API design; Jack contributed fields and invariants; GenAI-assisted drafting | Confirm human writing, revision, review, and testing |
| FoodOrder.java | Talal contributed class/API design; GenAI-assisted drafting | Confirm human writing, revision, review, and testing |
| DrinkOrder.java | Talal contributed class/API design; GenAI-assisted drafting | Confirm human writing, revision, review, and testing |
| Menu.java | Talal contributed application API design; Bryan contributed validation design; GenAI-assisted drafting | Confirm human writing, revision, review, and testing |
| OrderManager.java | Talal contributed application API design; Jack contributed state and invariants; GenAI-assisted drafting | Confirm human writing, revision, review, and testing |
| ConsoleApp.java | Talal contributed application API design; Bryan contributed validation design; GenAI-assisted drafting | Confirm human writing, revision, review, and testing |
| LinkedChainTest.java | Bryan contributed the specification test plan; GenAI-assisted test drafting | Confirm author(s), revisions, review, and test execution |
| OrderTest.java | Bryan contributed the specification test plan; GenAI-assisted test drafting | Confirm author(s), revisions, review, and test execution |
| FoodOrderTest.java | Bryan contributed the specification test plan; GenAI-assisted test drafting | Confirm author(s), revisions, review, and test execution |
| DrinkOrderTest.java | Bryan contributed the specification test plan; GenAI-assisted test drafting | Confirm author(s), revisions, review, and test execution |
| MenuTest.java | GenAI drafted a test class in this conversation; Bryan contributed the specification test plan | Confirm author(s), revisions, review, and test execution |
| OrderManagerTest.java | Bryan contributed the specification test plan; GenAI-assisted test drafting | Confirm author(s), revisions, review, and test execution |
| ConsoleAppTest.java | Bryan contributed the specification test plan; GenAI-assisted test drafting | Confirm author(s), revisions, review, and test execution |

Abigail contributed the specification’s system diagram, generic contract, and attribution/reflection work. These design and documentation contributions are separate from any file-level implementation contributions confirmed above.

### GenAI Use

GenAI assistance included:

- Class and API design suggestions.
- Substantial initial application and test-code drafting.
- Validation and edge-case ideas.
- README drafting and comparison with the specification.

This README was prepared after inspecting the uploaded application code and running compilation and console checks. The current JUnit passing status is reported by the team. Earlier tests against a temporary chain are not claimed as evidence for this repository.

The contribution table should be updated to reflect actual human writing, revisions, review, and testing while retaining accurate GenAI attribution.
