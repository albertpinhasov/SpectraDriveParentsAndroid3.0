package com.spectraparent.Helpers;

public class Const {
    public static final class RequestCode{
        public static final int GALLERY = 1;
        public static final int CAMERA = 2;
        public static final int PHOTO_CHOOSE = 3;
        public static final int PICK_FILE = 4;
        public static final int VIDEO_CHOOSE = 5;
        public static final int AUDIO_CHOOSE = 6;
        public static final int LOCATION_CHOOSE = 7;
        public static final int CONTACT_CHOOSE = 8;
    }

    public static final class FilesName{
        public static final String CAMERA_TEMP_FILE_NAME = "camera.jpg";
        public static final String AUDIO_TEMP_FILE_NAME = "voice.wav";
        public static final String VIDEO_TEMP_FILE_NAME = "video.mp4";
        public static final String SCALED_PREFIX = "scaled_";
        public static final String IMAGE_TEMP_FILE_NAME = "image_spika";
        public static final String TEMP_FILE_NAME = "temp.spika";
    }

    public static final class ContentTypes{
        public static final String IMAGE_JPG = "image/jpeg";
        public static final String IMAGE_PNG = "image/png";
        public static final String IMAGE_GIF = "image/gif";
        public static final String VIDEO_MP4 = "video/mp4";
        public static final String AUDIO_WAV = "audio/wav";
        public static final String AUDIO_MP3 = "audio/mp3";
        public static final String OTHER = "application/octet-stream";
    }
}