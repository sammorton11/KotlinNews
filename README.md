# KotlinNews

This is a news application that displays a list of news cards containing data from the NewsAPI website. The app uses a Model-View-ViewModel (MVVM) architecture and makes use of the following libraries:

Retrofit: For making network requests to the NewsAPI


Coroutines: For managing background tasks and offloading work from the main thread


XML: For designing the user interface


The app is divided into three packages: data, domain, and presentation. 

The data package contains the implementation of the Repository interface, which is responsible for fetching data from the NewsAPI and updating the app's LiveData objects with the results. 

The domain package contains the NewsApi interface, which defines the API endpoints and request parameters. 

The presentation package contains the ViewModel and Fragment classes, which handle the business logic and UI of the app, respectively.




To improve the app's testability and maintainability, I am planning to implement the following features:

Dagger Hilt for Dependency Injection: This will allow me to inject the Repository and NewsApi objects into the app's ViewModel and Fragment classes, making it easier to test these classes in isolation.
Additional unit tests for business logic functions: I will write unit tests to cover the business logic of the app, including the functions that parse the API response and update the app's LiveData objects.
Integration tests for the Repository and ViewModel: I will write integration tests to verify that the Repository and ViewModel are working correctly together.
Integration tests for the ViewModel and UI: I will write integration tests to verify that the ViewModel is correctly updating the app's UI.
End-to-End tests: I will write end-to-end tests to test the app's functionality from the user's perspective, including making network requests and verifying that the correct data is displayed on the screen.

The goal of this project is to create a well-designed, maintainable, and fully-tested news application that allows users to easily search for and browse the latest news articles.



Current Version UI:



![Screen Shot 2023-01-06 at 6 19 10 PM](https://user-images.githubusercontent.com/86651172/211121312-6462714a-b2f9-4c51-a265-2566c835756c.png) ![Screen Shot 2023-01-06 at 6 16 21 PM](https://user-images.githubusercontent.com/86651172/211121325-8a76a112-131d-43db-a53a-72179b151f34.png) ![Screen Shot 2023-01-06 at 6 18 40 PM](https://user-images.githubusercontent.com/86651172/211121332-da41113e-90f4-4b4f-bfa7-5692dba98e8f.png)

