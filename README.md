# OS_Scheduler
In this project we want to simulate scheduling on a 4-core CPU with various scheduling algorithms.

we use main thread as shared memory which contains the shared ready queue and waiting queue.

then 4 separated threads are created as processors, and they are given ready queue and waiting queue to schedule themselves.

in this system also exist resources that are needed by different types of processes which scheduler needs to manage.

# Algorithms provided in the project:
- FCFS
  - this algorithm only considers task priorities as the sorting factor
- SJF
  - this algorithm first sorts the ready queue by task burst time, and then considers the priority
- RR
  - this algorithm is similar to FCFS in terms of sorting, but it also has preemption so every task is given to a processor for a limited time ( = Quantum)
- HRRN
  - this algorithm first sorts the ready queue by (W + S) / S , and then considers the priority
    - W = Waiting Time
    - S = Burst Time

# FLOW

in main, there exist the ready queue and the waiting queue, and also the While loop which is the main part of scheduling is happening

while loop breaks when there is no Un-finished task, also when there is a deadlock


# HOW TO RUN
Run Main.java and in public void run(), change the function to your desired algorithm. ( on Processor.java line : 316)
 
