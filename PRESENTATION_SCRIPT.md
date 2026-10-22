# The Pass — 10-Minute Presentation Script & Demo Guide
**CS 2114 Deliverable 4: Final Build & Presentation**  
**Group 84:** Abigail, Bryan, Jack, and Talal  
**Target Duration:** ~10 minutes total (including live demo)  
**Slide Deck:** `The_Pass_Deliverable4_Presentation.pptx`  
**GitHub Repository:** `https://github.com/Talal288/cs2114-project2-group84`

---

## Presentation Overview & Speaker Assignments

| Section | Topic | Allocated Time | Primary Speaker(s) | Slides |
| :--- | :--- | :---: | :--- | :---: |
| **Intro** | Title & Repository Verification | ~0.5 min | **Abigail** | Slide 1 |
| **Part 1** | What You Built (Architecture, Generics, Stretch Goal) | ~2.0 min | **Abigail & Talal** | Slides 2–4 |
| **Part 2** | Live Demonstration | ~2.0 min | **Talal & Bryan** | Slide 5 |
| **Part 3** | What Went Well & What Didn't (Bugs & Generics) | ~2.0 min | **Jack** | Slides 6–7 |
| **Part 4** | Ethics Reflection on GenAI (Evidence, Risks, Future) | ~3.0–3.5 min | **Bryan & Abigail (all chime in)** | Slides 8–10 |
| **Outro** | Conclusion & Q&A | ~0.5 min | **All** | Slide 11 |

---

## Slide-by-Slide Spoken Script

### Slide 1: Title & Overview (0:00 – 0:30)
**Speaker: Abigail**
> *"Hello everyone and welcome to our Deliverable 4 presentation for Project 2: Building for Reuse. Our project is called **The Pass**, developed by Group 84: Bryan, Jack, Talal, and myself, Abigail.*
>
> *As shown on our title slide, our public GitHub repository is live at `github.com/Talal288/cs2114-project2-group84`. You can open this link in any browser or private window to inspect our full source code, our 104 passing JUnit tests, our comprehensive README with full attribution, and our system diagrams.*
>
> *Let's dive into what we built."*

---

### Slide 2: Part 1 — Single-User Restaurant Order Workflow (0:30 – 1:15)
**Speaker: Abigail**
> *"The Pass is an interactive console application designed to coordinate food and drink orders between dining tables, the kitchen dispatch, and the bar.*
>
> *Staff can add food items like burgers, fries, or salads, and drink items like water, coffee, or lemonade for any positive table number. Each placed order automatically receives a unique, sequential order ID, guaranteeing that identical menu items remain completely distinct. Pending orders are organized into two separate arrival-ordered queues: the kitchen queue and the bar queue. Staff can inspect these queues in real time to see position, order ID, table number, and whole elapsed wait minutes.*
>
> *When an order is prepped, staff can complete the oldest entry, moving it into a permanent, chronological sent-out log. If an order was entered by mistake, staff can cancel it directly by its displayed position without logging it."*

---

### Slide 3: Part 1 — Generic LinkedChain Reuse & Architecture (1:15 – 1:50)
**Speaker: Talal**
> *"The central architectural goal of this project was **building for reuse**. In our design diagram on Slide 3, you can see how we achieved this.*
>
> *Our `OrderManager` domain controller holds three distinct instances of the course-supplied `LinkedChain<T>` data structure:*
> 1. *`kitchen: LinkedChain<FoodOrder>`*
> 2. *`bar: LinkedChain<DrinkOrder>`*
> 3. *`sentOut: LinkedChain<Order>`*
>
> *Notice that we reused the exact same generic class across two distinct concrete element types—`FoodOrder` and `DrinkOrder`—as well as their common abstract parent, `Order`. Most importantly, our application classes interact with `LinkedChain` **strictly through its public API**—using `addToEnd`, `remove`, `get`, `size`, and `toArray`. Application classes never touch node links or internal pointers, and `LinkedChain` has zero awareness of restaurant tables, food items, or status logic. This is pure, defensible generic reuse."*

---

### Slide 4: Part 1 — Evolution from Spec & Stretch Goal (1:50 – 2:30)
**Speaker: Talal**
> *"How did the system evolve from our Deliverable 2 specification and Deliverable 3 prototype?*
>
> *In our initial spec, we thought we would need to edit `LinkedChain` by adding a tail reference, a `removeFirst()` method, and a `removeAt()` method. In our prototype and final build, we proved that modifying the data structure was unnecessary. By adapting `OrderManager` to call `addToEnd()` and `remove()`, we respected the generic contract completely.*
>
> *Furthermore, to achieve an **Exceeds** on working code, our team implemented our proposed stretch goal: **item customizations and special instructions**. Staff can now optionally enter culinary notes like 'no onions' or 'extra ice'. We updated `Order`, `FoodOrder`, and `DrinkOrder` with defensive 100-character boundary checks, preserved backward compatibility through constructor overloading, and integrated these notes seamlessly into all queue, table, and sent-out displays. Everything is fully tested and verified."*

---

### Slide 5: Part 2 — Live Demonstration (2:30 – 4:30)
**Primary Operators: Talal (driving console) & Bryan (narrating)**

*(Talal shares terminal running `java -cp bin ConsoleApp`)*

- **Action 1: Ingestion & Stretch Goal Customization**
  - **Bryan:** *"First, we will add an order for Table 4 using Command 1. We select 'burger', and for our stretch goal, we enter special instructions: 'no onions'. Next, we use Command 2 to add a drink for Table 4: 'lemonade' with 'extra ice'. Notice both are confirmed with sequential IDs 1 and 2."*
- **Action 2: Generic Queue Reuse & Queue Inspection**
  - **Bryan:** *"Now, Command 3 shows our kitchen queue holding our `FoodOrder`, and Command 4 shows our bar queue holding our `DrinkOrder`. Each display shows position, ID, table, customization, and elapsed wait time. This proves two different element types are independently held in separate `LinkedChain` instances."*
- **Action 3: Bad-Input Resilience Without Crashing**
  - **Bryan:** *"Next, let's test defensive resilience. In Command 1, if we type 'abc' or '-5' for the table, the app re-prompts for a valid whole, positive number. If we enter 'pizza', it rejects it as not on the menu. If we enter 'water' under food, it rejects the wrong category. When we type '7' to cancel and enter '99', it warns that the position is out of bounds and allows us to re-enter or type '0' to safely return to the menu without crashing."*
- **Action 4: Dispatch & Sent-Out Log**
  - **Bryan:** *"Finally, we complete the food order with Command 5 and the drink with Command 6. Command 10 shows our sent-out log, where both items now appear in completion order in our polymorphic `LinkedChain<Order>` log. Everything runs live and operates without defect."*

---

### Slide 6: Part 3 — Retrospective: What Went Well & What Didn't (4:30 – 5:30)
**Speaker: Jack**
> *"Looking back at our development journey from specification to prototype to final build, several things went exceptionally well.*
>
> *Our strict separation of concerns allowed us to write 104 unit tests covering every single public method across all classes. Our ID counter prevents duplicate items—such as two identical burgers for Table 4—from being conflated.*
>
> *However, we encountered real engineering hurdles. One significant bug in our prototype was the **LIFO vs. FIFO starter behavior**. The staff `LinkedChain.add()` method prepends to the front of the chain. In our earliest test runs, newer orders were being completed before older ones! We caught this in unit testing and immediately refactored all enqueue calls to `addToEnd()` to ensure true FIFO dispatch.*
>
> *We also battled input scanner buffering in `ConsoleApp`, where invalid tokens left trailing newlines that skipped subsequent prompts. We resolved this by centralizing on `readLine()` and trimming inputs."*

---

### Slide 7: Part 3 — What 'Reuse' and 'Generics' Mean Now (5:30 – 6:30)
**Speaker: Jack**
> *"Our understanding of 'reuse' and 'generics' changed dramatically between Week 6 and Week 9.*
>
> *In Week 6, we viewed generics mostly as syntax to avoid casting—like typing `List<String>` instead of `(String) list.get(0)`. We thought 'reuse' meant extending base classes through deep inheritance or copy-pasting containers.*
>
> *By Week 9, we realized that **true generic reuse is about establishing and respecting an uncoupled contract**. An unbounded `LinkedChain<T>` only relies on universal `Object` behavior like `.equals()`. Because `Order.equals()` compares unique order IDs, `LinkedChain` can locate and remove specific orders without needing any knowledge of tables, menus, or timestamps. Composition with generics gave us maximum flexibility with zero code duplication."*

---

### Slide 8: Part 4 — Ethics Reflection: Delegation vs. Verification (6:30 – 7:30)
**Speaker: Bryan**
> *"Now we move to Part 4: our Ethics Reflection on Generative AI, which is the most critical component of our analysis.*
>
> *Our team used GenAI as a brainstorming partner. We delegated initial boilerplate skeletons, repetitive equals/hashCode patterns, and ideas for text-alignment algorithms.*
>
> *However, we **never accepted GenAI code blindly**. The team manually verified every contract against CS 2114 requirements, verified that `LinkedChain` nodes were never leaked to callers, and ensured that all code compiled and ran on our local course setup."*

---

### Slide 9: Part 4 — Concrete Evidence: Prompt, AI Output & Team Verification (7:30 – 8:45)
**Speaker: Bryan**
> *"On Slide 9, we present a concrete example of raw GenAI output and how our team audited and corrected it.*
>
> *We asked GenAI: 'Write a JUnit test for Order that verifies invalid wait time throws an IllegalArgumentException.'*
>
> *The AI generated the code on the left: package-private `@Test`, using `assertThrows(IllegalArgumentException.class, () -> order.getWaitMinutes(999L))`. While this is valid modern Java, **it completely failed in our course environment**. Virginia Tech's `CS2-Support` library and grading harness rely on JUnit 4 and `student.TestCase`, which does not use lambda-based `assertThrows`.*
>
> *The team caught this failure during compilation. As shown on the right, we hand-refactored the test to extend `student.TestCase`, adopted the explicit `try { ... fail(); } catch` assertion idiom, and verified it directly inside Eclipse. This experience proved to us that AI lacks awareness of specialized course tooling and environmental constraints."*

---

### Slide 10: Part 4 — Over-Reliance Risks & Future Practices (8:45 – 9:30)
**Speaker: Abigail**
> *"Reflecting on this, where could over-reliance have hurt us?*
>
> *If a student relies on AI to write linked list code, they completely miss out on mastering pointer mechanics—drawing node diagrams, understanding null references, and managing edge cases like head and tail updates. Furthermore, if one teammate over-relies on AI, it creates an unfair skill disparity where teammates cannot explain or defend the codebase.*
>
> *For future projects, our team has committed to three practices:*
> 1. *Always draft class contracts and invariants on paper before writing code or consulting AI.*
> 2. *Enforce a strict 'verify-first' rule: every line must be traced and executed against local tests.*
> 3. *Practice collective code ownership through pair programming so every team member can defend any class live in lab."*

---

### Slide 11: Conclusion & Q&A (9:30 – 10:00)
**Speaker: Talal**
> *"To summarize: The Pass is a fully functional, defensively designed restaurant order system. We achieved true generic reuse across three distinct element types, implemented our stretch goal of item customizations, and backed our implementation with 104 passing tests.*
>
> *Our repository link is `github.com/Talal288/cs2114-project2-group84`. Thank you for your time, and we would be happy to answer any questions!"*

---

## Live Demo Quick-Reference Card (Keep Open During Presentation!)

```
Terminal Command:
java -cp bin ConsoleApp

Key Sequences:
1. Add Food (MVP + Stretch):
   > 1
   > 4
   > burger
   > no onions

2. Add Drink (MVP + Stretch):
   > 2
   > 4
   > lemonade
   > extra ice

3. View Queues (Generic Reuse):
   > 3   (Kitchen view)
   > 4   (Bar view)

4. Bad Input Handling:
   > 1
   > abc
   > -3
   > 2
   > pizza
   > 

5. Cancellation Boundary:
   > 7
   > 99  (Out of bounds)
   > 0   (Go back)

6. Dispatch & Log:
   > 5   (Complete food)
   > 6   (Complete drink)
   > 10  (View sent-out log)
   > 0   (Exit)
```
