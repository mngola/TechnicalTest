#Question A
###Assumptions
* Line coordinates are integers
* Two lines overlap if they share a point e.g. (1,5) and (5,10)
###Test Notes
* Test 0
  * Covers a typical case
  * Input: (1,5) and (2,6)
  * Expected output: Lines overlap
* Test 1
  * Covers a typical case
  * Input: (1,5) and (6,8)
  * Expected output: Lines do not overlap
* Test 2
  * Single point lines
  * Input: (1,1) and (1,1)
  * Expected output: Lines overlap
* Test 3
  * Expected handling of negative numbers
  * Input: (-3,-1) and (0,5)
  * Expected output: Lines do not overlap
* Test 4
  * Input with line end before start 
  * Input: (5,1) and (8,5)
  * Expected output: Lines overlap

#Question B
###Assumptions
* Input version numbers only contain numbers and points
* Version number cannot be negative
###Implementation Details
Input strings are first sanitized by removing the points. 
The input strings are checked for length discrepancies 
and the shorter string is padded with 0s as appropriate.
The inputs are converted to ints and compared. 
###Test Notes
* Test 0
  * Covers a typical case for greater than
  * Input: 1.2 and 1.1
  * Expected output: 1.2 is greater than 1.1
* Test 1
  * Covers the reverse of test 0 for less than
  * Input: 1.1 and 1.2
  * Expected output: 1.1 is less than 1.2
* Test 2
  * Covers a typical case for equal to
  * Input: 1.0 and 1.0
  * Expected output: 1.0 is equal to 1.0
* Test 3
  * Test that padding produces the correct answer
  * Input: 2.0 and 1.01
  * Expected output: 2.0 is greater than 1.01
* Test 4
  * Tests that comparison works with multiple minor versions 
  * Input: 1.0.0 and 1.0.1
  * Expected output: 1.0.0 is less than 1.0.1
* Test 5
  * Tests that trailing 0s don't change the answer
  * Input: 0.0 and 0.0000
  * Expected output: 0.0 is equal to 0.0000
  
#Question C
###Assumptions
* The cache stores a key-value pair (both integers)
* The operations put(key,value) and get(key) are exposed to the user
* Locations are represented as points on a grid (latitude, longitude)
* Capacity and expiry time is uniform across local caches
* Expiry time is measured in seconds
###Implementation Details
* A LRU Cache was implemented in LRUCache.java using a hashmap and a doubly linked list.
  * The cache uses a cleanup thread to manage expiry. Each node stores a last access time,
  if the time since its last access is greater than expiry time then the node will be deleted.
  * A NoSuchElementException is thrown if the key is not found in the cache
* The GeoDistLRUCache.java is used to implement the other features of cache
  * When calling put, the value is entered into every cache in the network. This maintains data 
  consistency across regions and replicates data in real time.
  * When calling get, a location needs to be supplied along with the key. The location is used to find
  the nearest cache. Should a cache fail, the next nearest cache would be automatically used. 
###Test Notes
The CacheTester creates 3 threads to represent the clients accessing the cache. Each thread is given
a random location. Each client attempts to put 10 items into the cache, meaning only the last 4 values
should remain at the end of the operation. The client then attempts to get the 10 values it inserted 
expecting 6 failures and 4 successes. The client then waits for the expiry time and attempts to get 
all 10 values, expecting 10 failures. 