## Transaction system

### Run with maven
* Run `mvn build`
* Start the application with `mvn spring-boot:run`

### Testing
* Run the unit tests with `mvn test`

Using apache benchmark we can send multiple concurrent request to see how the system behaves.

This can be used via a docker image. Pull the image with  `docker pull jordi/ab`.
Then, for example, running `docker run --rm jordi/ab -k -c 100 -n 100000 http://172.17.0.1:8080/balances/1` which tests the balance route with 100 requests at a time for a total of 100 000 requests.

```
Document Path:          /balances/1
Document Length:        38 bytes

Concurrency Level:      100
Time taken for tests:   45.875 seconds
Complete requests:      100000
Failed requests:        0
Keep-Alive requests:    0
Total transferred:      14300000 bytes
HTML transferred:       3800000 bytes
Requests per second:    2179.85 [#/sec] (mean)
Time per request:       45.875 [ms] (mean)
Time per request:       0.459 [ms] (mean, across all concurrent requests)
Transfer rate:          304.41 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.3      0       7
Processing:     1   45  34.5     34     468
Waiting:        1   39  25.5     33     391
Total:          1   46  34.5     35     468


```