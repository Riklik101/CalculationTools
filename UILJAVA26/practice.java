package UILJAVA26;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class practice {

	public static void main(String[] args) {
		System.out.println("=== AP CSA Java Practice File ===");
		System.out.println("This file demonstrates classes, constructors, lists, sorting, and Java limitations.\n");

		basicsAndVariablesDemo();
		constructorsAndObjectsDemo();
		arraysAndListsDemo();
		sortingDemo();
		inheritanceAndPolymorphismDemo();
		commonConfusionsAndLimitsDemo();

		System.out.println("\n=== End of Practice Run ===");
	}

	private static void basicsAndVariablesDemo() {
		System.out.println("[1] Variables + Basic Syntax");

		// Primitive variables (store raw values)
		int wholeNumber = 42;
		double decimalNumber = 3.14;
		boolean isReady = true;
		char letter = 'A';

		// Reference variable (stores an object reference, not the object value itself)
		String message = "Hello, AP CSA";

		// final means value cannot be reassigned after initialization
		final int DAYS_IN_WEEK = 7;

		// Type casting: careful with loss of precision
		int fromDouble = (int) decimalNumber; // becomes 3, not 3.14

		System.out.println("int: " + wholeNumber);
		System.out.println("double: " + decimalNumber);
		System.out.println("boolean: " + isReady);
		System.out.println("char: " + letter);
		System.out.println("String: " + message);
		System.out.println("final constant: " + DAYS_IN_WEEK);
		System.out.println("cast double -> int: " + fromDouble + "\n");
	}

	private static void constructorsAndObjectsDemo() {
		System.out.println("[2] Classes + Constructors + Instance vs Static Variables");

		BankAccount defaultAccount = new BankAccount();
		BankAccount namedAccount = new BankAccount("Eklavya", 100.0);

		// Instance method calls
		defaultAccount.deposit(50.0);
		namedAccount.withdraw(40.0);

		// Static method/variable usage (class-level)
		System.out.println("Accounts created (static count): " + BankAccount.getTotalAccounts());

		System.out.println(defaultAccount);
		System.out.println(namedAccount);
		System.out.println();
	}

	private static void arraysAndListsDemo() {
		System.out.println("[3] Arrays + ArrayList");

		// ARRAY: fixed size after creation
		int[] scores = {88, 94, 76, 100};
		int total = 0;
		for (int score : scores) {
			total += score;
		}
		double average = (double) total / scores.length;
		System.out.println("Array scores: " + Arrays.toString(scores));
		System.out.println("Average score: " + average);

		// ARRAYLIST: dynamic size (can grow/shrink)
		ArrayList<String> topics = new ArrayList<>();
		topics.add("Classes");
		topics.add("ArrayList");
		topics.add("Sorting");
		topics.add("Inheritance");

		topics.remove("Sorting");
		topics.add(2, "Searching");

		System.out.println("ArrayList topics: " + topics);
		System.out.println("First topic: " + topics.get(0));
		System.out.println("Total topics: " + topics.size() + "\n");
	}

	private static void sortingDemo() {
		System.out.println("[4] Sorting");

		// Sorting primitive array
		int[] nums = {9, 2, 7, 1, 5};
		Arrays.sort(nums);
		System.out.println("Sorted int[]: " + Arrays.toString(nums));

		// Sorting objects by Comparable (natural order)
		ArrayList<Student> students = new ArrayList<>();
		students.add(new Student("Mia", 91));
		students.add(new Student("Zane", 84));
		students.add(new Student("Aria", 95));

		Collections.sort(students); // uses Student.compareTo (by grade)
		System.out.println("Students sorted by grade ascending: " + students);

		// Sorting objects by custom Comparator (different order)
		students.sort(Comparator.comparing(Student::getName));
		System.out.println("Students sorted by name ascending: " + students + "\n");
	}

	private static void inheritanceAndPolymorphismDemo() {
		System.out.println("[5] Inheritance + Interface + Polymorphism");

		// Polymorphism: Vehicle reference points to Car object
		Vehicle myVehicle = new Car("Toyota", 2020, 4);

		// Dynamic method dispatch: Car.describe() is called at runtime
		System.out.println(myVehicle.describe());

		// Interface reference points to class implementing interface
		Drivable driverView = (Drivable) myVehicle;
		driverView.startEngine();
		driverView.stopEngine();
		System.out.println();
	}

	private static void commonConfusionsAndLimitsDemo() {
		System.out.println("[6] Commonly Confused Concepts + Java Limitations");

		// 1) Integer division truncates decimal part
		int a = 7;
		int b = 2;
		System.out.println("7 / 2 using int = " + (a / b)); // 3
		System.out.println("7 / 2.0 using double = " + (a / 2.0)); // 3.5

		// 2) String comparison: use equals, not ==
		String s1 = new String("java");
		String s2 = new String("java");
		System.out.println("s1 == s2 ? " + (s1 == s2));         // false (different objects)
		System.out.println("s1.equals(s2) ? " + s1.equals(s2)); // true (same content)

		// 3) Java is pass-by-value (including object references)
		Counter counter = new Counter(10);
		tryToChangePrimitive(a);
		tryToChangeObject(counter);
		System.out.println("After methods, primitive a is still: " + a); // unchanged
		System.out.println("After methods, counter value is: " + counter.getValue());

		// 4) Java limitation: ArrayList cannot hold primitive types directly
		// ArrayList<int> invalid = new ArrayList<>(); // does NOT compile
		ArrayList<Integer> validList = new ArrayList<>(); // wrapper class works
		validList.add(10);

		// 5) Java limitation: no multiple inheritance of classes
		// class Child extends ParentA, ParentB {} // does NOT compile
		// Workaround: implement multiple interfaces.

		// 6) Java limitation: array size is fixed after creation
		int[] fixed = new int[3];
		System.out.println("Fixed array length: " + fixed.length + " (cannot resize)");

		// 7) Null reference limitation/risk
		String maybeNull = null;
		if (maybeNull != null) {
			System.out.println(maybeNull.length());
		} else {
			System.out.println("Guarded against NullPointerException with a null check.");
		}
	}

	private static void tryToChangePrimitive(int number) {
		// number is a COPY of caller's variable
		number = 999;
	}

	private static void tryToChangeObject(Counter c) {
		// c is a copy of the reference, but it still points to the same object
		// so mutating object state affects the original object
		c.setValue(999);
	}
}

class BankAccount {
	// static variable belongs to the class (shared by all objects)
	private static int totalAccounts = 0;

	// instance variables belong to each object
	private String owner;
	private double balance;

	// no-arg constructor
	public BankAccount() {
		this("Unknown", 0.0); // constructor chaining
	}

	// parameterized constructor
	public BankAccount(String owner, double startingBalance) {
		this.owner = owner;
		this.balance = Math.max(0.0, startingBalance); // prevent negative starting balance
		totalAccounts++;
	}

	public void deposit(double amount) {
		if (amount > 0) {
			balance += amount;
		}
	}

	public boolean withdraw(double amount) {
		if (amount > 0 && amount <= balance) {
			balance -= amount;
			return true;
		}
		return false;
	}

	public static int getTotalAccounts() {
		return totalAccounts;
	}

	@Override
	public String toString() {
		return "BankAccount{owner='" + owner + "', balance=" + balance + "}";
	}
}

class Student implements Comparable<Student> {
	private String name;
	private int grade;

	public Student(String name, int grade) {
		this.name = name;
		this.grade = grade;
	}

	public String getName() {
		return name;
	}

	public int getGrade() {
		return grade;
	}

	@Override
	public int compareTo(Student other) {
		// Natural ordering for Collections.sort(students)
		return Integer.compare(this.grade, other.grade);
	}

	@Override
	public String toString() {
		return name + "(" + grade + ")";
	}
}

interface Drivable {
	void startEngine();

	void stopEngine();
}

class Vehicle {
	protected String brand;
	protected int year;

	public Vehicle(String brand, int year) {
		this.brand = brand;
		this.year = year;
	}

	public String describe() {
		return "Vehicle: " + brand + " (" + year + ")";
	}
}

class Car extends Vehicle implements Drivable {
	private int doors;

	public Car(String brand, int year, int doors) {
		super(brand, year); // call parent constructor
		this.doors = doors;
	}

	@Override
	public String describe() {
		return "Car: " + brand + " (" + year + "), doors=" + doors;
	}

	@Override
	public void startEngine() {
		System.out.println("Engine started.");
	}

	@Override
	public void stopEngine() {
		System.out.println("Engine stopped.");
	}
}

class Counter {
	private int value;

	public Counter(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
