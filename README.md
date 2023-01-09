# KotlinNews

This is a news application that displays a list of news cards containing data from the NewsAPI website. The app uses a Model-View-ViewModel (MVVM) architecture and makes use of the following libraries:

- Retrofit: For making network requests to the NewsAPI
- Coroutines: For managing background tasks and offloading work from the main thread
- XML: For designing the user interface


The app is divided into three packages: data, domain, and presentation. 
- The data package contains the implementation of the Repository interface, which is responsible for fetching data from the NewsAPI and updating the app's LiveData objects with the results. 

- The domain package contains the NewsApi interface, which defines the API endpoints and request parameters. 

- The presentation package contains the ViewModel and Fragment classes, which handle the business logic and UI of the app, respectively.




To improve the app's testability and maintainability, I am planning to implement the following features:

- Dagger Hilt for Dependency Injection: This will allow me to inject the Repository and NewsApi objects into the app's ViewModel and Fragment classes, making it easier to test these classes in isolation.

- Room Database: This will allow the app to quickly display data to the user, even when there is no internet connection. It can also improve the user experience by reducing the amount of time it takes to load data. By using the Room database, I can also make the app more testable by providing a local data source for unit tests. This will allow me to test the app's logic without making actual network requests.

- Additional unit tests for business logic functions

- Integration tests


The goal of this project is to create a well-designed, maintainable, and fully-tested news application that allows users to easily search for and browse the latest news articles.

