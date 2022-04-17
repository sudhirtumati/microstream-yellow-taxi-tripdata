Sample application to demonstrate the performance issue I encountered while learning microstream.

## Prerequisites

Before running the application, source data file must be available on classpath. Source file can be downloaded from
[https://nyc-tlc.s3.amazonaws.com/trip+data/yellow_tripdata_2021-07.csv](https://nyc-tlc.s3.amazonaws.com/trip+data/yellow_tripdata_2021-07.csv)

## Run book

### Step 1 - Data loading

- Run `Application.java`
- As the application is running for the first time, data is loaded from provided input file
- 2021 July trip data file contains 2.8+ million records. It takes about **5 minutes** to load this data on Windows
  laptop with application running on WSL2 (Ubuntu 20.04). Considering the amount of data to be processed, **5 minutes**
  seems reasonable.
- Stop the application

### Step 2 - Calculate total distance covered

- Run `Application.java`
- As data is loaded during previous run, data is not loaded again
- Application takes about 30 seconds to start - relatively slower when compared to startup time during **Step 1**
- After another minute or so, an out of memory error is thrown during total trip distance computation
    - It seems entire data (4 GB) is being loaded into memory during computation

## Questions

- Why entire data is being loaded into memory? This seems very inefficient especially for microservices running in
    containerized environment
- Did I miss certain optimization opportunities with my implementation?