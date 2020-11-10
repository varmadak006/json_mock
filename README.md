
## Running

Run this using [sbt](http://www.scala-sbt.org/).

```
sbt run
```

And then go to http://localhost:9000 to see the running web application.

## Test Run Results
curl -X GET "http://localhost:9000/posts"

=>[{"id":0,"title":"13","author":"CIQ","views":10,"reviews":31},{"id":3,"title":"31","author":"CIQ31","views":100,"reviews":1},{"id":4,"title":"foo","body":"bar","userId":1},{"id":5,"title":"foo5","body":"bar5","userId":5}]

curl -X GET "http://localhost:9000/posts/0"
=>{"id":0,"title":"13","author":"CIQ","views":10,"reviews":31}


curl -X GET "http://localhost:9000/posts?_sort=id&_order=desc"
=>[{"id":5,"title":"foo5","body":"bar5","userId":5},{"id":4,"title":"foo","body":"bar","userId":1},{"id":3,"title":"31","author":"CIQ31","views":100,"reviews":1},{"id":0,"title":"13","author":"CIQ","views":10,"reviews":31}]


curl -X GET "http://localhost:9000/posts?_sort=id&_order=asc"
=>[{"id":0,"title":"13","author":"CIQ","views":10,"reviews":31},{"id":3,"title":"31","author":"CIQ31","views":100,"reviews":1},{"id":4,"title":"foo","body":"bar","userId":1},{"id":5,"title":"foo5","body":"bar5","userId":5}]

curl -X GET "http://localhost:9000/posts?_sort=id&_order=dsc"
=>please enter valid orderBy clause => dsc

curl -X PATCH -H "Content-Type: application/json" -d '{
  "title": "3"
}' "http://localhost:9000/posts/3"
=>{"title":"3","id":3}

curl -X GET "http://localhost:9000/posts?_sort=title&_order=desc"
=>[{"id":5,"title":"foo5","body":"bar5","userId":5},{"id":4,"title":"foo","body":"bar","userId":1},{"id":3,"title":"3","author":"CIQ31","views":100,"reviews":1},{"id":0,"title":"13","author":"CIQ","views":10,"reviews":31}]
=>

curl -X GET "http://localhost:9000/posts?_sort=body&_order=desc"
=>[{"id":5,"title":"foo5","body":"bar5","userId":5},{"id":4,"title":"foo","body":"bar","userId":1},{"id":0,"title":"13","author":"CIQ","views":10,"reviews":31},{"id":3,"title":"3","author":"CIQ31","views":100,"reviews":1}]


curl -X GET "http://localhost:9000/posts?id=0"
=>[{"id":0,"title":"13","author":"CIQ","views":10,"reviews":31}]

curl -X GET "http://localhost:9000/posts?id=0&id=1"
=>[{"id":0,"title":"13","author":"CIQ","views":10,"reviews":31}]

curl -X GET "http://localhost:9000/posts?id=0&id=3"
=>[{"id":0,"title":"13","author":"CIQ","views":10,"reviews":31},{"id":3,"title":"3","author":"CIQ31","views":100,"reviews":1}]


curl -X GET "http://localhost:9000/posts?id=0&author=CIQ"
=>[{"id":0,"title":"13","author":"CIQ","views":10,"reviews":31}]

curl -X GET "http://localhost:9000/posts?q=CIQ"
=>[{"id":0,"title":"13","author":"CIQ","views":10,"reviews":31},{"id":3,"title":"3","author":"CIQ31","views":100,"reviews":1}]

curl -X POST -H "Content-Type: application/json" -d '{
  "id": 3,
  "name": "Inception",
  "director": "Christopher Nolan",
  "rating": 9.0
}' "http://localhost:9000/movies"
=> {"id":3,"name":"Inception","director":"Christopher Nolan","rating":9.0}

curl -X POST -H "Content-Type: application/json" -d '{
  "id": 3,
  "name": "Inception",
  "director": "Christopher Nolan",
  "rating": 9.0
}' "http://localhost:9000/movies"
=> 3 id already exists


curl -X PUT -H "Content-Type: application/json" -d '{
  "name": "PUT Inception",
  "director": "PUT Christopher Nolan",
  "rating": 10.0
}' "http://localhost:9000/movies/3"
=> {"name":"PUT Inception","director":"PUT Christopher Nolan","rating":10.0,"id":3}

curl -X POST -H "Content-Type: application/json" -d '{
  "id": 3,
  "name": "Inception",
  "director": "Christopher Nolan",
  "rating": 9.0
}' "http://localhost:9000/movies"

=> movies 3 already exists 

curl -X POST -H "Content-Type: application/json" -d '{
  "name": "Inception",
  "director": "Christopher Nolan",
  "rating": 9.0
}' "http://localhost:9000/movies"

=> {"name":"Inception","director":"Christopher Nolan","rating":9.0,"id":5}

 curl -X PATCH -H "Content-Type: application/json" -d '{
  "name": "PATCH Inception",
  "director": "PATCH Christopher Nolan"
}' "http://localhost:9000/movies/5"

=>{"name":"PATCH Inception","director":"PATCH Christopher Nolan","id":5}

curl -X DELETE "http://localhost:9000/movies/5"
=> deleted id 5


curl -X DELETE "http://localhost:9000/movies/5"
=> not found id 5


