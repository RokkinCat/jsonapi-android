# jsonapi-android

A Java library that can be used to convert [JSON API](http://jsonapi.org/) responses to Java Objects. I’m not going to go into detail about the JSON API specification. To learn more about it, visit their specification document [here](http://jsonapi.org/format/).

# Getting Started
This library is available through jCenter by adding this line to your app dependencies
```
compile 'com.jsonapi:jsonapi-java:0.1.0'
```


## example

Here is a typical jsonapi server response

	{
	  "data": [{
	    "type": "articles",
	    "id": "1",
	    "attributes": {
	      "title": "JSON API paints my bikeshed!"
	    },
	    "relationships": {
	      "author": {
	        "data": { "type": "people", "id": "9" }
	      }
	    },
	    "links": {
	      "self": "http://example.com/articles/1"
	    }
	  }],
	  "included": [{
	    "type": "people",
	    "id": "9",
	    "attributes": {
	      "first-name": "Dan",
	      "last-name": "Gebhardt",
	      "twitter": "dgeb"
	    },
	    "links": {
	      "self": "http://example.com/people/9"
	    }
	  }]
	}	

This response is describing an article written by Dan Gebhardt. In the root level attribute `data` you see we have the list of articles from this response (in this case it’s only 1). In the root level attribute `included` you see we have a list of people that are related in some way to the list of articles from data. Here are two POJO’s for representing this data.

	public class Article {
	    String title;
	
	    @Relationship(“author”)
	    Person author;
	}

	public class Person {
			@JsonApiName(“first-name”)
			String firstName;
			
			@JsonApiName(“last-name”)			
			String lastName;
			
			String twitter;
	}

Two things to notice here. `@Relationship` and `@JsonApiName`. These are the two key annotations for using this library. `@JsonApiName` tells the library when a Java instance variable’s name is not formatted the same as the JSON API response. It is similar to GSON’s `@SerializedName`. Use this when you don’t want to name your variables the same as the server response.

`@Relationship` tells the library that this attribute should be coming from the `relationships` array instead of the `attributes` array. 

Now lets check out how we turn that JSON API response into useable Java objects.

	JsonApiParser parser = new JsonApiParser();
	List<Article> articles = (List<Article>)parser.parse(json_api_response, Article.class);

That’s it! You now have a list of Article’s and the author’s who wrote them. 


## License

JSONAPI is available under the MIT license. See the LICENSE file for more info.
