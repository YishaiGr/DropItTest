# DropItTest
MVVM architecture, using Dagger2, Retrofit2, Data Binding, Navigation and more.

This app is updating contact and address details, adding bags and sending this information to the server. 
After the information was sent the app display a review window.

The app is structured by feature folders:

DropItTest ->

    address ->  (Contains Address Fragment and ViewModel)
    
    bags ->  (Contains Bags Fragment, ViewModel and two RecyclerView Adapters) 
    
    contact ->  (Contains Contact Fragment and ViewModel)
    
    data ->  (This data layer contians the Repository, the entities and the Retrofit Service and Client)
    
    di ->  (Dependency Injection package that contains AppComponent, SubComponent and ServiceModule that provides the Retrofit Service)
    
    main ->  (Contains the Activity and ViewModel)
    
    review ->  (Contains Review Fragment and RecyclerView Adapter)
    
    util ->  (Contains all the shared files between the components)

Application Flow:
![appFlow](https://user-images.githubusercontent.com/41877281/73127159-fa145d80-3fc4-11ea-8744-d60fa07e3c1b.png)

Contact Details (ContactFragment):

![Contact Details](https://user-images.githubusercontent.com/41877281/73127251-33010200-3fc6-11ea-818d-0e8be8268126.png)

Address (AddressFragment):

![Address](https://user-images.githubusercontent.com/41877281/73127276-a3a81e80-3fc6-11ea-880d-4dfa5e0297e3.png)

Bags (BagsFragment):

![Bags](https://user-images.githubusercontent.com/41877281/73127281-b589c180-3fc6-11ea-88f2-a1299cced927.png)

Review Drop (ReviewFragment):

![Review](https://user-images.githubusercontent.com/41877281/73127285-c33f4700-3fc6-11ea-880e-7dc4db9b1178.png)

Connection Error (ErrorFragment):

![Connection Error](https://user-images.githubusercontent.com/41877281/73127301-d3efbd00-3fc6-11ea-80ca-039ae5810365.png)

Dependencies:

RecyclerView, Navigation, Dagger2, Gson, Retrofit2, OkHttp3, Dagger2, Lottie.
