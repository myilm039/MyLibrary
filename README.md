# MyLibrary
My Library is a Book collection mobile application, built using Android Studio
 and Java. For storing data, it uses SQLite Database.
  The user is able to reach the data of any book, 
  using Google Books API. From the Book Details page,
   the user can add the book to their Collection or Wishlist, and
    also see and reach similar books. From the same page, the user can preview or buy the book, from the webpage of the book in Google Books website.
     After the user adds a book
     to their collection, they are able to give rating, and add comments to the book. 
 
## API Reference

#### Bookshelf

```http
  https://developers.google.com/books/docs/v1/reference#bookshelf
```

| Method | REST URI     | Description                |
| :-------- | :------- | :------------------------- |
| `list` | `GET  /users/userId/bookshelves` | Retrieves a list of public Bookshelf resource for the specified user. |
| `get` | `GET  /users/userId/bookshelves/shelf` | Retrieves a specific Bookshelf resource for the specified user. |

#### Volume

```http
  https://developers.google.com/books/docs/v1/reference#volume
```

| Method | REST URI      | Description                       |
| :-------- | :------- | :-------------------------------- |
| `get`      | `GET  /volumes/volumeId` | Retrieves a Volume resource based on ID. |
| `list`      | `GET  /volumes?q={search terms}` | Performs a book search. |



#### Bookshelves.volumes

```http
  https://developers.google.com/books/docs/v1/reference#bookshelves.volumes
```

| Method | REST URI      | Description                       |
| :-------- | :------- | :-------------------------------- |
| `list`      | `GET  /users/userId/bookshelves/shelf/volumes` | Retrieves volumes in a specific bookshelf for the specified user. |


#### Mylibrary.bookshelves

```http
  https://developers.google.com/books/docs/v1/reference#mylibrary.bookshelves
```

| Method | REST URI      | Description                       |
| :-------- | :------- | :-------------------------------- |
| `addVolume`      | `POST  /mylibrary/bookshelves/shelf/addVolume` | Adds a volume to a bookshelf. |
| `clearVolumes`      | `POST  /mylibrary/bookshelves/shelf/clearVolumes` |  Clears all volumes from a bookshelf.|
| `get`      | `GET  /mylibrary/bookshelves/shelf` | Retrieves metadata for a specific bookshelf belonging to the authenticated user. |
| `list`      | `GET  /mylibrary/bookshelves` | Retrieves a list of bookshelves belonging to the authenticated user. |
| `moveVolume`      | `POST  /mylibrary/bookshelves/shelf/moveVolume` | Moves a volume within a bookshelf. |
| `removeVolume`      | `POST  /mylibrary/bookshelves/shelf/removeVolume` | Removes a volume from a bookshelf. |


#### Mylibrary.bookshelves.volumes 

```http
  https://developers.google.com/books/docs/v1/reference#bookshelves.volumes
```

| Method | REST URI      | Description                       |
| :-------- | :------- | :-------------------------------- |
| `list`      | `GET  /mylibrary/bookshelves/shelf/volumes` | Gets volume information for volumes on a bookshelf. |

 
## Database

To store the user collection and wishlist data, the app uses SQLite DB. It stores
the information with the help of SQLiteOpenHelper class.

Database Structure
<img src="https://github.com/myilm039/MyLibrary/blob/development/Screenshots/Screenshot_3.png">
Collection Table
<img src="https://github.com/myilm039/MyLibrary/blob/development/Screenshots/Screenshot_2.png">
Wishlist Table
<img src="https://github.com/myilm039/MyLibrary/blob/development/Screenshots/Screenshot_1.png">


## Features

- Book collection creation and storage
- Book wishlist creation and storage
- Search any book by typing either book's title, author or publisher
- Search a book from a specific genre, using category buttons in the search page
- Give rating and add comments to a book in their collection
- Preview or Buy a book from the page of the book in Google Books.

## Screenshots

<table>
  <tr>
    <td><img src="https://github.com/myilm039/MyLibrary/blob/development/Screenshots/Screenshot_1641770159.png" width=240 height=480></td>
    <td><img src="https://github.com/myilm039/MyLibrary/blob/development/Screenshots/Screenshot_1641771354.png" width=240 height=480></td>
    <td><img src="https://github.com/myilm039/MyLibrary/blob/development/Screenshots/Screenshot_1641771465.png" width=240 height=480></td>
  </tr>
  
  <tr>
    <td><img src="https://github.com/myilm039/MyLibrary/blob/development/Screenshots/Screenshot_1641771883.png" width=240 height=480></td>
    <td><img src="https://github.com/myilm039/MyLibrary/blob/development/Screenshots/Screenshot_1641771908.png" width=240 height=480></td>
    <td><img src="https://github.com/myilm039/MyLibrary/blob/development/Screenshots/Screenshot_1641771936.png" width=240 height=480></td>
  </tr>
  
   <tr>
    <td><img src="https://github.com/myilm039/MyLibrary/blob/development/Screenshots/Screenshot_1641771505.png" width=240 height=480></td>
    <td><img src="https://github.com/myilm039/MyLibrary/blob/development/Screenshots/Screenshot_1641771728.png" width=240 height=480></td>
    <td><img src="https://github.com/myilm039/MyLibrary/blob/development/Screenshots/Screenshot_1641771842.png" width=240 height=480></td>
  </tr>
  
   
  
 </table>
 
## Feedback

If you wish to share any feedback regarding this project, you can reach me by my email at myilm039@uottawa.ca


