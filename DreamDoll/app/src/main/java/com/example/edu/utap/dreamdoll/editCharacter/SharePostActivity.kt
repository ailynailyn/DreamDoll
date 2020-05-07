package com.example.edu.utap.dreamdoll

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File


// EditFaceFrag.kt & edit_features.xml
class SharePostActivity : BaseActivity() {

    private lateinit var bitmap: Bitmap
    private lateinit var mStorageRef : StorageReference
    private var username = ""
    private val db = Firebase.firestore


    override fun onBackPressed() {
        super.onBackPressed()
        Log.d("share post activity","onbackpressed")
    }

//    private fun getImageData(bmp: Bitmap) {
//        var bao = ByteArrayOutputStream()
//        bmp.compress(Bitmap.CompressFormat.PNG, 100, bao)
//        bmp.recycle()
//        var byteArray = bmp.toByteArray()
//        var imageB64 = Base64.encodeToString(byteArray, Base64.URL_SAFE)
//    }

//    fun uploadFile() {
////
////        val file: Uri = Uri.fromFile(File("path/to/images/rivers.jpg"))
////        val riversRef: StorageReference = mStorageRef.child("images/rivers.jpg")
////
////        riversRef.putFile(file)
////            .addOnSuccessListener { taskSnapshot -> // Get a URL to the uploaded content
////                val downloadUrl: Uri = taskSnapshot.getDownloadUrl()
////
////            }
////            .addOnFailureListener {
////                // Handle unsuccessful uploads
////                // ...
////            }
////    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.share_post)
        displayOptionsMenu(true)
        mStorageRef = FirebaseStorage.getInstance().getReference()
        var imageByteArray = ByteArray(1)

        // Get extra information.
        var extras = intent.extras
        if(extras != null) {
            imageByteArray = intent.extras!!.getByteArray("imageByteArray")!!
            Log.d("oncreate share post activity", "$imageByteArray")
            if(imageByteArray != null) {
                var imageView = findViewById<ImageView>(R.id.sharePostImage)
                bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray!!.size)
                imageView.setImageBitmap(bitmap)

            }
        }
        var userID = mAuth.currentUser!!.uid

        // Need to set the username.
        val userRef = db.collection("users").document(userID)
        userRef.get()
            .addOnSuccessListener { doc ->
                if(doc != null) {
                    var temp = doc["username"]
                    Log.d("Found username $temp", "Success")
                    username = temp.toString()
                } else {
                    Log.d("Couldn't find username", "FAILED")
                }
            }
            .addOnFailureListener {
                Log.d("Couldn't find username", "FAILED")
            }

        var captionET = findViewById<EditText>(R.id.sharePostCaption)
        var shareButton = findViewById<Button>(R.id.sharePostButton)
        shareButton.setOnClickListener {
            var caption = captionET.text.toString()
            Log.d("Going to submit to database", "caption: $caption")



            var postID = db.collection("newsfeed").document().id
            val imageRef = mStorageRef.child(postID + ".png")
            var uploadTask = imageRef.putBytes(imageByteArray)
            uploadTask
                .addOnSuccessListener {
                    Log.d("Added image to storage.", "success")
                }
                .addOnFailureListener{
                    Log.d("unable to add image to storeage", "failed")
                }

            var userFields = hashMapOf("caption" to caption, "likes" to 0, "pictureID" to "$postID.png"
            )

                // add to user posts.
            db.collection("users").document(userID)
                .collection("posts").document(postID).set(userFields)
                .addOnSuccessListener {
                    Log.d("Was able to add post to users posts", "success")
                    // Add to newsfeed database.
                    var newsfeedFields = hashMapOf("caption" to caption,
                        "likes" to 0, "pictureID" to "$postID.png", "timestamp" to "xx-xx-xxxx-xx-xxxx",           // NEED TO PASS CORRECT THING TO PICTURE ID
                        "userID" to userID, "username" to username)
                    db.collection("newsfeed").document(postID)
                        .set(newsfeedFields)
                        .addOnSuccessListener {
                            Log.d("Was able to add post to newsfeed posts", "success")
                            // Go to the newsfeed.
                            val intent = Intent(this, NewsfeedActivity::class.java)
                            intent.putExtra("title", "Added post. Headed to newsfeed")
                            startActivity(intent)
                        }
                        .addOnFailureListener {
                            Log.d("Was unable to add post to newsfeed posts", "failed")
                        }
                }
                .addOnFailureListener {
                    Log.d("Could not add post to user's posts.", "failed")
                }
            // add to newsfeed database
        }



//        beginFeaturesFrag()

    }

}
