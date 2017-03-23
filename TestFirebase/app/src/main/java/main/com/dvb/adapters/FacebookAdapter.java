package main.com.dvb.adapters;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import main.com.dvb.Constants;
import main.com.dvb.R;
import main.com.dvb.homePageFragments.FacebookFragment;
import main.com.dvb.pojos.FB.FBCommentBean;
import main.com.dvb.pojos.FB.FBCommentBeanParent;
import main.com.dvb.pojos.FB.FBLikesBeanParent;
import main.com.dvb.pojos.FB.FBPhotosBean;
import main.com.dvb.pojos.FB.FacebookBean;

/**
 * Created by AIA on 11/16/16.
 */

public class FacebookAdapter extends RecyclerView.Adapter<FacebookAdapter.MyViewHolder>{

    private List<FacebookBean> facebookBeanList = new ArrayList<>();

    public View mView;
    public boolean loadingMore;
    ArrayList<FBCommentBean> fbCommentBeanArrayList;
    FBCommentBeanParent fbCommentBeanParent;
    CommentsListAdapter commentsListAdapter;
    ListView commentsList;
    ProgressBar progressBar;
    ArrayList<FBPhotosBean> fbPhotosBeanArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView message;
        public TextView time;
        public TextView story;
//        public ImageView image;
        public ImageView picture;
        public TextView likes;
        public TextView comments;
        public ImageView userImg;
        public TextView feedDate;
        public ImageView likeAction;
        public ImageView commentAction;


        public MyViewHolder(View view) {
            super(view);
            mView = view;

            message = (TextView) mView.findViewById(R.id.user_messag_facebook);
//            image = (ImageView) mView.findViewById(R.id.userImg_facebook);
            picture = (ImageView) mView.findViewById(R.id.picture_facebook);
            likes = (TextView) mView.findViewById(R.id.likes_facebook);
            comments = (TextView) mView.findViewById(R.id.comments_facebook);
            userImg = (ImageView) mView.findViewById(R.id.userImg_facebook);
            feedDate = (TextView) mView.findViewById(R.id.feedDate_facebook);
            likeAction = (ImageView) mView.findViewById(R.id.makeLike_facebook);
            commentAction = (ImageView) mView.findViewById(R.id.makeComment_facebook);

        }
    }

    public FacebookAdapter(List<FacebookBean> facebookBean) {

        this.facebookBeanList.addAll(facebookBean);

    }
    public void resetData(List<FacebookBean> facebookBean){
        facebookBeanList.clear();
        this.facebookBeanList.addAll(facebookBean);
    }

    @Override
    public FacebookAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.facebook_item, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(FacebookAdapter.MyViewHolder holder, int position) {

        final FacebookBean facebookBean = facebookBeanList.get(position);
        holder.message.setText(facebookBean.getUserMessage());
        holder.feedDate.setText(dateConvertor(facebookBean.getUserCreated_time()));

        final FBLikesBeanParent fbLikesBeanParent = facebookBean.getFbLikesBeanParent();
        holder.likes.setText(fbLikesBeanParent.getTotalCount()+"");

        fbCommentBeanParent = facebookBean.getFbCommentBeanParent();
        holder.comments.setText(fbCommentBeanParent.getTotalCount()+"");

        if(facebookBean.getPicture() != null && !facebookBean.getPicture().equalsIgnoreCase("")) {
            holder.picture.setVisibility(View.VISIBLE);
            Glide.with(FacebookFragment.facebookFragment)
                    .load(facebookBean.getPicture())
//                    .placeholder(R.mipmap.ic_launcher)
                    .into(holder.picture);

            holder.picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fbPhotosBeanArrayList = new ArrayList<>();
//                    new loadFeedPictures().execute(facebookBean.getUserId());

                }
            });
        }
        else
            holder.picture.setVisibility(View.GONE);
        holder.userImg.setImageBitmap(Constants.profileImage);

//        holder.commentAction.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                final Dialog dialog = new Dialog(context, android.R.style.Theme);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.comments_layout);
//                dialog.setTitle("Custom Dialog Example");
//
//                fbCommentBeanArrayList = new ArrayList<FBCommentBean>();
//                fbCommentBeanArrayList.addAll(fbCommentBeanParent.getCommentsList());
//                commentsList = (ListView) dialog.findViewById(R.id.comments_list);
//                progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar);
//                commentsListAdapter = new CommentsListAdapter(fbCommentBeanArrayList, Dashboard_main.context);
//                commentsList.setAdapter(commentsListAdapter);
//
//                //setting  listener on scroll event of the list
//                commentsList.setOnScrollListener(new AbsListView.OnScrollListener() {
//
//                    @Override
//                    public void onScrollStateChanged(AbsListView view, int scrollState) {
//                    }
//
//                    @Override
//                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//
//                        //what is the bottom item that is visible
//                        int lastInScreen = firstVisibleItem + visibleItemCount;
//
//                        //is the bottom item visible & not loading more already? Load more!
//                        if ((lastInScreen == totalItemCount) && !(loadingMore)) {
//                            new loadMoreComments().execute(fbCommentBeanParent.getNext());
//                        }
//                    }
//                });
//
//                dialog.show();
//            }
//        });



//        holder.likeAction.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                Bundle params = new Bundle();
////                params.putString("object", "http://samples.ogp."+Constants.facebookPageOwner+"/"+facebookBean.getUserId());
////                "/{object-id}/likes"
//                new GraphRequest(
//                        FacebookFragment.facebookFragment.accessToken,
//                        "/"+ Constants.facebookPageOwner+"/"+facebookBean.getUserId()+"/likes",
//                        null,
//                        HttpMethod.POST,
//                        new GraphRequest.Callback() {
//                            public void onCompleted(GraphResponse response) {
//                                try {
//                                    JSONObject jObjResponse = new JSONObject(String.valueOf(response.getJSONObject()));
//
//                                    Log.e("","");
//                                }
//                                catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                ).executeAsync();
//            }
//        });

    }


    public class loadMoreComments extends AsyncTask<String, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingMore = true;
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... urls) {

            try {
                try {
                    JSONObject commentObj = new JSONObject(FacebookFragment.facebookFragment.webServices.getRequest(urls[0]));
//                    JSONObject commentObj = object.getJSONObject("comments");
                    JSONArray commentArray = commentObj.getJSONArray("data");
                    for (int j = 0; j < commentArray.length(); j++) {
                        JSONObject commentObj2 = commentArray.getJSONObject(j);
                        FBCommentBean fbCommentBean = new FBCommentBean();
                        fbCommentBean.createdTime = commentObj2.getString("created_time");
                        //                    fbCommentBean.userName = commentObj2.getString("name");
                        //                    fbCommentBean.userNameId = commentObj2.getString("id");
                        fbCommentBean.message = commentObj2.getString("message");
                        fbCommentBean.commentId = commentObj2.getString("id");

                        JSONObject obj = commentObj2.getJSONObject("from");
                        fbCommentBean.userNameId = obj.getString("id");
                        fbCommentBean.userName = obj.getString("name");

                        fbCommentBeanArrayList.add(fbCommentBean);
                    }

                    if(commentObj.has("paging")){
                        JSONObject commentPagingObj = commentObj.getJSONObject("paging");
                        if(commentPagingObj.has("cursors")){
                            JSONObject commentCursorObj = commentPagingObj.getJSONObject("cursors");
                            fbCommentBeanParent.after = commentCursorObj.getString("after");
                            fbCommentBeanParent.before = commentCursorObj.getString("before");
                        }else{
                            fbCommentBeanParent.after ="";
                            fbCommentBeanParent.before = "";
                        }


                        if(commentPagingObj.has("next")){
                            fbCommentBeanParent.next = commentPagingObj.getString("next");
                        }else{
                            fbCommentBeanParent.next = "";
                        }
                    }else{
                        fbCommentBeanParent.after = "";
                        fbCommentBeanParent.before = "";
                        fbCommentBeanParent.next = "";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            loadingMore = false;
            commentsListAdapter.resetData(fbCommentBeanArrayList);
            commentsListAdapter.notifyDataSetChanged();


        }
    }

    public class loadFeedPictures extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            try {
                response = FacebookFragment.facebookFragment.webServices.getRequest("https://graph.facebook.com/v2.2/"+urls[0]+"?fields=attachments&fields=attachments&access_token="+FacebookFragment.facebookFragment.accessToken.getToken());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject attachmentObj = jsonObject.getJSONObject("attachments");
                JSONArray jsonArray = attachmentObj.getJSONArray("data");

                for(int i =0;i<jsonArray.length();i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    if(object.has("subattachments")){
                        JSONObject subattachmentsArray = object.getJSONObject("subattachments");
                        JSONArray dataArray = subattachmentsArray.getJSONArray("data");
                        for(int j =0;j<dataArray.length();j++){
                            JSONObject mediaObj = dataArray.getJSONObject(j);
                            if(mediaObj.has("media")){
                                mediaObj = mediaObj.getJSONObject("media");
                                JSONObject imageObj = mediaObj.getJSONObject("image");
                                FBPhotosBean fbPhotosBean = new FBPhotosBean();
                                fbPhotosBean.photoURI = imageObj.getString("src");
                                fbPhotosBean.photoWidth = imageObj.getInt("width");
                                fbPhotosBean.photoHeight = imageObj.getInt("height");

                                fbPhotosBeanArrayList.add(fbPhotosBean);
                            }
                        }
                    }else{
                        JSONObject mediaObj = object.getJSONObject("media");
                        if(mediaObj.has("image")){
                            JSONObject imageObj = mediaObj.getJSONObject("image");
                            FBPhotosBean fbPhotosBean = new FBPhotosBean();
                            fbPhotosBean.photoURI = imageObj.getString("src");
                            fbPhotosBean.photoWidth = imageObj.getInt("width");
                            fbPhotosBean.photoHeight = imageObj.getInt("height");

                            fbPhotosBeanArrayList.add(fbPhotosBean);
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            super.onPostExecute(result);
        }
    }

    public String dateConvertor(String dateValue){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
        Date date = null;
        try {
            date = format.parse(dateValue);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(date == null){
            return "";
        }else{
            return dateToString(date);
        }

    }

    public String dateToString(Date date){
        String datetime="";
        SimpleDateFormat myFormat = new SimpleDateFormat("MMM dd, ''yy hh:mm a");
        try {
            datetime = myFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datetime;
    }


    @Override
    public int getItemCount() {
        return facebookBeanList.size();
    }
}
