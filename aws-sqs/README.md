You can see if the queue was created by using this command:

``aws --endpoint-url=http://localhost:4566 sqs list-queues``

To send a message use the following curl command:

``
  curl -X POST \
    http://localhost:8080/queue \
    -H 'content-type: application/json' \
    -d '{
   "message":"Hello there"
  }'
``