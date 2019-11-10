# applitools-hackathon

src\test\java\TraditionalTests.java - Traditional Automation

src\test\java\VisualAITests.java - Visual AI Tests using Applitools

Issues in Traditional Automation
=================================
Test 01. Login page UI elements validation
------------------------------------------
1. Find the locators for each and every element that needs to be tested
2. Easier to validate if an image exists, but not possible to test if it's the same image
3. Also, it is not possible to validate if the image is present but moved to different element

Test 02. Data Driven Test
------------------------------------------
Need to write assertions differently for each data variation that fails as well as the one that succeeds

Test 03. Table Sort
------------------------------------------
While it can be automated, it's tedious. Had to locate each and every locator in the table, iterate through them to find values, store them and compare them at the end.

Test 04. Canvas Chart
------------------------------------------
The Canvas could not be validated using just Selenium Webdriver. Can detect if the canvas is available but cannot detect that it is changed as javascript drives the data.

Test 05. Dynamic Content
------------------------------------------
1. Presence or absence of image can be validated but not if it is moved.

Benefits of Applitools Eyes
=================================
1. Need not spend time in identifying locators, copy-paste text from application
2. Easier to set type of validation from entire window to element or from check everything to just layout
3. Review what has changed visually with the highlights
4. Ability to validate charts, graphics.
5. Ability to ignore changes, set/review/update baseline from a single location
