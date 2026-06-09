import os
from statistics import mean, stdev
from scipy.stats import norm

def read_grades(filename):
    """Read grades from file and return as list of floats."""
    try:
        with open(filename, 'r') as f:
            grades = [float(line.strip()) for line in f if line.strip()]
        return grades
    except FileNotFoundError:
        print(f"Error: {filename} not found.")
        return []

def write_grades(filename, grades):
    """Write curved grades to file."""
    with open(filename, 'w') as f:
        for grade in grades:
            f.write(f"{grade:.2f}\n")

def bell_curve(grades, target_mean, target_std):
    """
    Apply Gaussian/bell curve transformation using normal distribution.
    Uses the probability density function to map grades from original distribution
    to a new normal distribution with target mean and standard deviation.
    Does not reduce any grades, caps at 100.
    
    Formula: p(x) = (1/(σ√(2π))) * e^(-(1/2)*((x-μ)/σ)²)
    where x is the value, μ is the mean, and σ is the standard deviation.
    """
    if len(grades) < 2:
        return grades[:]
    
    # Calculate original distribution parameters
    orig_mean = mean(grades)
    orig_std = stdev(grades)
    
    if orig_std == 0:
        # All grades are the same, no curve needed
        return grades[:]
    
    curved = []
    for grade in grades:
        # Find percentile of grade in original distribution (CDF)
        percentile = norm.cdf(grade, loc=orig_mean, scale=orig_std)
        
        # Map percentile to new distribution with target parameters (inverse CDF)
        new_grade = norm.ppf(percentile, loc=target_mean, scale=target_std)
        
        # Don't reduce grades, cap at 100
        new_grade = max(grade, min(100, new_grade))
        curved.append(new_grade)
    
    return curved

def linear_curve(grades, points_to_add):
    """
    Apply linear curve: add fixed points to each grade.
    Caps at 100.
    """
    curved = [min(100, grade + points_to_add) for grade in grades]
    return curved

def target_curve(grades, target_score):
    """
    Apply target-based curve: scale based on target score and highest grade.
    Formula: new_grade = old_grade + (target_score - highest_grade)
    This allows the highest grade to reach the target score.
    Caps at 100.
    """
    if not grades:
        return grades
    
    highest = max(grades)
    curve_adjustment = target_score - highest
    
    # Only apply if it's a positive adjustment (curving up)
    if curve_adjustment > 0:
        curved = [min(100, grade + curve_adjustment) for grade in grades]
    else:
        # If target is below highest grade, don't reduce any grades
        curved = grades[:]
    
    return curved

def display_grade_stats(grades):
    """Display statistics about current grades."""
    if not grades:
        return
    
    avg = mean(grades)
    highest = max(grades)
    lowest = min(grades)
    
    if len(grades) > 1:
        std_dev = stdev(grades)
        print(f"  Average: {avg:.2f} | Highest: {highest:.2f} | Lowest: {lowest:.2f} | Std Dev: {std_dev:.2f}")
    else:
        print(f"  Average: {avg:.2f} | Highest: {highest:.2f} | Lowest: {lowest:.2f}")

def main():
    script_dir = os.path.dirname(os.path.abspath(__file__))
    input_file = os.path.join(script_dir, 'preCurveGrades.txt')
    output_file = os.path.join(script_dir, 'postCurveGrades.txt')
    
    # Read initial grades
    grades = read_grades(input_file)
    
    if not grades:
        print("No grades to curve.")
        return
    
    print(f"Loaded {len(grades)} grades from preCurveGrades.txt")
    print("\nInitial Grade Statistics:")
    display_grade_stats(grades)
    
    print("\n" + "="*60)
    print("CURVING FUNCTION - Enter 0 to skip any step")
    print("="*60)
    
    # Step 1: Bell Curve
    print("\nSTEP 1: BELL CURVE (Gaussian Distribution)")
    print("This transforms grades to follow a normal distribution with target mean and std dev.")
    print("Enter target mean (e.g., 75), or 0 to skip:")
    
    try:
        target_mean = float(input("Target mean: "))
        if target_mean != 0:
            print("Enter target standard deviation (e.g., 10):")
            target_std = float(input("Target std dev: "))
            if target_std > 0:
                grades = bell_curve(grades, target_mean, target_std)
                print(f"✓ Applied bell curve (target: μ={target_mean}, σ={target_std})")
                print("  Stats after bell curve:")
                display_grade_stats(grades)
            else:
                print("⊘ Invalid std dev, skipped bell curve")
        else:
            print("⊘ Skipped bell curve")
    except ValueError:
        print("⊘ Invalid input, skipped bell curve")
    
    # Step 2: Linear Curve
    print("\nSTEP 2: LINEAR CURVE (Fixed Point Increase)")
    print("This adds a fixed number of points to every grade.")
    print("Enter the number of points to add (e.g., 3 for +3 points), or 0 to skip:")
    
    try:
        linear_points = float(input("Linear curve points to add: "))
        if linear_points != 0:
            grades = linear_curve(grades, linear_points)
            print(f"✓ Applied linear curve (+{linear_points} points)")
            print("  Stats after linear curve:")
            display_grade_stats(grades)
        else:
            print("⊘ Skipped linear curve")
    except ValueError:
        print("⊘ Invalid input, skipped linear curve")
    
    # Step 3: Target-based Curve
    print("\nSTEP 3: TARGET-BASED CURVE (Curve-breaker Opportunity)")
    print("This curve allows you to set a target score that the highest grade should reach.")
    print("Formula: All grades shift up so the highest grade reaches your target.")
    print("Enter target score (e.g., 85), or 0 to skip:")
    
    try:
        target_score = float(input("Target score for highest grade: "))
        if target_score != 0:
            grades = target_curve(grades, target_score)
            print(f"✓ Applied target curve (highest grade target: {target_score})")
            print("  Stats after target curve:")
            display_grade_stats(grades)
        else:
            print("⊘ Skipped target curve")
    except ValueError:
        print("⊘ Invalid input, skipped target curve")
    
    # Write results
    print("\n" + "="*60)
    print("FINAL RESULTS")
    print("="*60)
    print("Final Grade Statistics:")
    display_grade_stats(grades)
    
    write_grades(output_file, grades)
    print(f"\n✓ Curved grades written to postCurveGrades.txt")

if __name__ == "__main__":
    main()
