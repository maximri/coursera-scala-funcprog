package funsets

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 * - run the "test" command in the SBT console
 * - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {


  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
   *
   * Operators
   * - test
   * - ignore
   * - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  test("string take") {
    val message = "hello, world"
    assert(message.take(5) == "hello")
  }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  test("adding ints") {
    assert(1 + 2 === 3)
  }


  import funsets.FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }

  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   * val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets {
    val s0 = singletonSet(0)
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
    val s999 = singletonSet(999)
    val sMinus999 = singletonSet(-999)
  }

  /**
   * This test is currently disabled (by using "ignore") because the method
   * "singletonSet" is not yet implemented and the test would fail.
   *
   * Once you finish your implementation of "singletonSet", exchange the
   * function "ignore" by "test".
   */
  test("singletonSet(1) contains 1") {

    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3". 
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton")
    }
  }

  test("union contains all elements") {
    new TestSets {
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

  test("intersect contains common elements") {
    new TestSets {
      val s = intersect(s1, s2)
      assert(!contains(s, 1), "intersect 1")

      val uninonOfOneAndTWo = union(s1, s2)
      val intersectTwo = intersect(uninonOfOneAndTWo, s2)
      assert(contains(intersectTwo, 2), "intersect 2")
    }
  }

  test("diff contains not common elements") {
    new TestSets {
      val oneAndTwo = diff(s1, s2)
      assert(contains(oneAndTwo, 1), "diff 1")
      assert(!contains(oneAndTwo, 2), "diff 2")
      val twoAndThree = union(s2, s3)

      assert(contains(diff(oneAndTwo, twoAndThree), 1), "diff 1 & 3")
    }
  }


  test("filter should returns the subset of a given set for which a given function holds.") {
    new TestSets {
      val biggerThanOne = filter(union(s1, s2), _ > 1)

      assert(contains(biggerThanOne, 2), "filter bigger than 1 ")
      assert(!contains(biggerThanOne, 1), "filter bigger than 1 wont contain 1")

    }
  }

  test("forall should check if for every element in the set, the result applied functions by a given function is in bound -+1000 ") {
    new TestSets {
      val zeroAnd999 = union(s0, s999)

      assert(forall(zeroAnd999, _ < 900) === false, "all in bound and lower tha  900")
      assert(forall(zeroAnd999, _ => true) === false, "all in bound ")
    }
  }

  test("exists should check if there is at least one element in the set that the result applied functions by a given function is in bound -+1000 ") {
    new TestSets {
      val oneAnd999 = union(s1, s999)

      assert(exists(oneAnd999, _ < 900) === true, "one in bound and lower than  900")
      assert(forall(oneAnd999, _ % 2 == true) === false, "no one is even")
    }
  }

  test("map should construct a new Set of applied given function") {
    new TestSets {
      val oneAndTwo = union(s1, s2)
      val addThreeForEachElement: Set = map(oneAndTwo, _ + 3)
      assert(contains(addThreeForEachElement, 4), " 1 + 3 = 4 ")
      assert(contains(addThreeForEachElement, 5), " 2 + 3 = 5 ")
      assert(!contains(addThreeForEachElement, 1), "shouldn't contain 1")

    }
  }

}
