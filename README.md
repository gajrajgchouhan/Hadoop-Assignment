### Assignment

A dataset contains transaction id followed by price of items purchased. 
e.g. 1: 100 200 400 500
The syntax is txid: p1 p2 p3 where txid is transaction id and pi denotes price. Each transaction can have variable number of items. 

1) Write a sequential program to generate the input data:
create 10million transaction records each containing a variable number of items randomly generated between 1 and 50, the price of each item is another random variable whose range is 100 to 5000.

2) Store the text file in HDFS.

3) Write a mapreduce program that partitions the text file into 5 classes as follows: class 1 contains transactions s.t. the item count is between 1-10, class2 for 11-20, and so on till class5 having 41-50 item count; and computes the total amount obtained for each class. 

input:  
1: 100 200 400 500
2: 10 50 5 25 89 20 35 91 78 82 150 125
3: 100 300

Here tx 1 and tx 3 belong to class 1 as the item count for each is between 1 - 10, while tx 2 belongs to class 2. The total amount from the sales for class 1 is the sum of the costs of all the items from tx 1 and tx 3. 

e.g. output 
class1, 1600  
class2, 760

#### Output

file - `part-r-0000`

```
Class1  28037128510
Class2  79033028706
Class3  130212783758
Class4  180928360907
Class5  232005308376
```

#### Random Data Generation - cpp file.
#### Hadoop Files - .java file.

![Screenshot from 2021-08-07 21-13-36](https://user-images.githubusercontent.com/54475046/128606069-cc7a2fa4-2db2-4ced-a493-b3d46f6735e6.png)

