public class GuestComixAPI implements ComixAPI{
    ComicController ComixController;
    UserController UserController;
    UserComixAPI userComixAPI;

    login(String username);
    /*
        comicDAO: ComicDAO
        userDAO: UserDAO
        UserComixAPI : userComixAPI

        setSortStragegy(strategy)
        setSearchStragegy(strategy)

        login(String username): User

        executeSearch(int userId, keyword) : Comic[ ]

        browse(publisher=, series=, volume=, issue=) :Comic[ ]
        generateStatistics(User user) :tuple(int, float)
     */
}