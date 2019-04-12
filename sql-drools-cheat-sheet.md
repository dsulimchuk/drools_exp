Following Schema is used for this examples (you can reason about it as both SQL tables or Drools Facts):
* **DailyActivity** represents activity performed by employee in particular day, it has  following attributes:
 * workingDayId
 * employeeActivity
 * employee
 * durationInSeconds

* **WorkingDay** represents particular working day of particular employee, it has following attributes:
 * workingDayId
 * dayOfWeek
 * employee
 * workingDay

* * * *
**TASK**:

    Find all Social Networking Activities

**SQL**: 
```SQL
SELECT * FROM DailyActivity WHERE employeeActivity = 'Social Networking'
```
**DROOLS**:
```java
$activity : DailyActivity( employeeActivity == "Social Networking")
```  
* * * *
**TASK**:

    Find all Social Networking Activities that are longer than 10 seconds

**SQL**:
```sql
SELECT * FROM DailyActivity WHERE employeeActivity = 'Social Networking' and durationInSeconds > 10
```
**DROOLS**:
```java
$activity : DailyActivity( employeeActivity == "Social Networking", durationInSeconds > 10)
```
* * * *
**TASK**:

    Find all Playing Tennis activities that happen during Monday

**SQL**:
```sql    
SELECT * FROM DailyActivity D JOIN WorkingDay W 
    ON (D.workingDayId = W.workingDayId and W.dayOfWeek = 'Monday')
    WHERE employeeActivity = 'Playing Tennis'
```    
**DROOLS**:
```java
$tennis_activity : DailyActivity( employeeActivity == "Playing Tennis")
$monday : WorkingDay( dayOfWeek == "Monday", this.workingDayId == $tennis_activity.workingDayId)
```
* * * *
**TASK**:

    Find all Working Days where someone played tennis

**SQL**:
```sql
SELECT * from WorkingDay WHERE EXISTS 
  (SELECT 1 FROM DailyActivity 
    WHERE DailyActivity.workingDayId = WorkingDay.workingDayId and employeeActivity = 'Playing Tennis'
  )
```

**DROOLS**:
```java
$working_day : WorkingDay()
exists DailyActivity( employeeActivity == "Playing Tennis", this.workingDayId == $working_day.workingDayId)
```
* * * *

**TASK**:

    Find all Working Days where noone played tennis

**SQL**:
```sql
SELECT * from WorkingDay WHERE NOT EXISTS 
  (SELECT 1 FROM DailyActivity 
    WHERE DailyActivity.workingDayId = WorkingDay.workingDayId and employeeActivity = 'Playing Tennis'
  )
```    

**DROOLS**:
```java
$working_day : WorkingDay()
not DailyActivity( employeeActivity == "Playing Tennis", this.workingDayId == $working_day.workingDayId)
```
* * * *
**TASK**:

    Find all Working Days where total time of lunch took more than 1 hour

**SQL**:
```sql
WITH T as (
    SELECT workingDayId FROM DailyActivity
    WHERE employeeActivity = 'Taking Lunch'
    GROUP BY workingDayId
    HAVING SUM(durationInSeconds) > 3600
    )
    SELECT W.* from WorkingDay W join T on W.workingDayId = T.workingDayId
```

**DROOLS**:
```java
$working_day : WorkingDay()
accumulate( 
    DailyActivity( this.workingDayId == $working_day.workingDayId, employeeActivity == "Taking Lunch", 
    $duration : durationInSeconds );
    $total : sum($duration); $total > 3600 );
```      
* * * *