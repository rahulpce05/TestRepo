Feature: Validating Place APIs

Scenario: Verify if place is being successfully added using addPlaceAPI
Given add Place request Payload
When user calls addPlaceAPI with POST http request
Then api call is success with status code 200
And "status" in response body is "OK"
And "scope" in response body is "APP"