/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * ColorConversionCode.java
 * Copyright (C) 2022 University of Waikato, Hamilton, New Zealand
 */

package adams.data.opencv;

/**
 * Codes for converting color spaces.
 *
 * https://docs.opencv.org/4.x/d8/d01/group__imgproc__color__conversions.html
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public enum ColorConversionCode {

  COLOR_BGR2BGRA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR2BGRA),
  COLOR_RGB2RGBA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_RGB2RGBA),
  COLOR_BGRA2BGR(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGRA2BGR),
  COLOR_RGBA2RGB(org.bytedeco.opencv.global.opencv_imgproc.COLOR_RGBA2RGB),
  COLOR_BGR2RGBA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR2RGBA),
  COLOR_RGB2BGRA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_RGB2BGRA),
  COLOR_RGBA2BGR(org.bytedeco.opencv.global.opencv_imgproc.COLOR_RGBA2BGR),
  COLOR_BGRA2RGB(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGRA2RGB),
  COLOR_BGR2RGB(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR2RGB),
  COLOR_RGB2BGR(org.bytedeco.opencv.global.opencv_imgproc.COLOR_RGB2BGR),
  COLOR_BGRA2RGBA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGRA2RGBA),
  COLOR_RGBA2BGRA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_RGBA2BGRA),
  COLOR_BGR2GRAY(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR2GRAY),
  COLOR_RGB2GRAY(org.bytedeco.opencv.global.opencv_imgproc.COLOR_RGB2GRAY),
  COLOR_GRAY2BGR(org.bytedeco.opencv.global.opencv_imgproc.COLOR_GRAY2BGR),
  COLOR_GRAY2RGB(org.bytedeco.opencv.global.opencv_imgproc.COLOR_GRAY2RGB),
  COLOR_GRAY2BGRA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_GRAY2BGRA),
  COLOR_GRAY2RGBA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_GRAY2RGBA),
  COLOR_BGRA2GRAY(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGRA2GRAY),
  COLOR_RGBA2GRAY(org.bytedeco.opencv.global.opencv_imgproc.COLOR_RGBA2GRAY),
  COLOR_BGR2BGR565(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR2BGR565),
  COLOR_RGB2BGR565(org.bytedeco.opencv.global.opencv_imgproc.COLOR_RGB2BGR565),
  COLOR_BGR5652BGR(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR5652BGR),
  COLOR_BGR5652RGB(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR5652RGB),
  COLOR_BGRA2BGR565(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGRA2BGR565),
  COLOR_RGBA2BGR565(org.bytedeco.opencv.global.opencv_imgproc.COLOR_RGBA2BGR565),
  COLOR_BGR5652BGRA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR5652BGRA),
  COLOR_BGR5652RGBA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR5652RGBA),
  COLOR_GRAY2BGR565(org.bytedeco.opencv.global.opencv_imgproc.COLOR_GRAY2BGR565),
  COLOR_BGR5652GRAY(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR5652GRAY),
  COLOR_BGR2BGR555(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR2BGR555),
  COLOR_RGB2BGR555(org.bytedeco.opencv.global.opencv_imgproc.COLOR_RGB2BGR555),
  COLOR_BGR5552BGR(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR5552BGR),
  COLOR_BGR5552RGB(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR5552RGB),
  COLOR_BGRA2BGR555(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGRA2BGR555),
  COLOR_RGBA2BGR555(org.bytedeco.opencv.global.opencv_imgproc.COLOR_RGBA2BGR555),
  COLOR_BGR5552BGRA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR5552BGRA),
  COLOR_BGR5552RGBA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR5552RGBA),
  COLOR_GRAY2BGR555(org.bytedeco.opencv.global.opencv_imgproc.COLOR_GRAY2BGR555),
  COLOR_BGR5552GRAY(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR5552GRAY),
  COLOR_BGR2XYZ(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR2XYZ),
  COLOR_RGB2XYZ(org.bytedeco.opencv.global.opencv_imgproc.COLOR_RGB2XYZ),
  COLOR_XYZ2BGR(org.bytedeco.opencv.global.opencv_imgproc.COLOR_XYZ2BGR),
  COLOR_XYZ2RGB(org.bytedeco.opencv.global.opencv_imgproc.COLOR_XYZ2RGB),
  COLOR_BGR2YCrCb(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR2YCrCb),
  COLOR_RGB2YCrCb(org.bytedeco.opencv.global.opencv_imgproc.COLOR_RGB2YCrCb),
  COLOR_YCrCb2BGR(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YCrCb2BGR),
  COLOR_YCrCb2RGB(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YCrCb2RGB),
  COLOR_BGR2HSV(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR2HSV),
  COLOR_RGB2HSV(org.bytedeco.opencv.global.opencv_imgproc.COLOR_RGB2HSV),
  COLOR_BGR2Lab(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR2Lab),
  COLOR_RGB2Lab(org.bytedeco.opencv.global.opencv_imgproc.COLOR_RGB2Lab),
  COLOR_BGR2Luv(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR2Luv),
  COLOR_RGB2Luv(org.bytedeco.opencv.global.opencv_imgproc.COLOR_RGB2Luv),
  COLOR_BGR2HLS(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR2HLS),
  COLOR_RGB2HLS(org.bytedeco.opencv.global.opencv_imgproc.COLOR_RGB2HLS),
  COLOR_HSV2BGR(org.bytedeco.opencv.global.opencv_imgproc.COLOR_HSV2BGR),
  COLOR_HSV2RGB(org.bytedeco.opencv.global.opencv_imgproc.COLOR_HSV2RGB),
  COLOR_Lab2BGR(org.bytedeco.opencv.global.opencv_imgproc.COLOR_Lab2BGR),
  COLOR_Lab2RGB(org.bytedeco.opencv.global.opencv_imgproc.COLOR_Lab2RGB),
  COLOR_Luv2BGR(org.bytedeco.opencv.global.opencv_imgproc.COLOR_Luv2BGR),
  COLOR_Luv2RGB(org.bytedeco.opencv.global.opencv_imgproc.COLOR_Luv2RGB),
  COLOR_HLS2BGR(org.bytedeco.opencv.global.opencv_imgproc.COLOR_HLS2BGR),
  COLOR_HLS2RGB(org.bytedeco.opencv.global.opencv_imgproc.COLOR_HLS2RGB),
  COLOR_BGR2HSV_FULL(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR2HSV_FULL),
  COLOR_RGB2HSV_FULL(org.bytedeco.opencv.global.opencv_imgproc.COLOR_RGB2HSV_FULL),
  COLOR_BGR2HLS_FULL(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR2HLS_FULL),
  COLOR_RGB2HLS_FULL(org.bytedeco.opencv.global.opencv_imgproc.COLOR_RGB2HLS_FULL),
  COLOR_HSV2BGR_FULL(org.bytedeco.opencv.global.opencv_imgproc.COLOR_HSV2BGR_FULL),
  COLOR_HSV2RGB_FULL(org.bytedeco.opencv.global.opencv_imgproc.COLOR_HSV2RGB_FULL),
  COLOR_HLS2BGR_FULL(org.bytedeco.opencv.global.opencv_imgproc.COLOR_HLS2BGR_FULL),
  COLOR_HLS2RGB_FULL(org.bytedeco.opencv.global.opencv_imgproc.COLOR_HLS2RGB_FULL),
  COLOR_LBGR2Lab(org.bytedeco.opencv.global.opencv_imgproc.COLOR_LBGR2Lab),
  COLOR_LRGB2Lab(org.bytedeco.opencv.global.opencv_imgproc.COLOR_LRGB2Lab),
  COLOR_LBGR2Luv(org.bytedeco.opencv.global.opencv_imgproc.COLOR_LBGR2Luv),
  COLOR_LRGB2Luv(org.bytedeco.opencv.global.opencv_imgproc.COLOR_LRGB2Luv),
  COLOR_Lab2LBGR(org.bytedeco.opencv.global.opencv_imgproc.COLOR_Lab2LBGR),
  COLOR_Lab2LRGB(org.bytedeco.opencv.global.opencv_imgproc.COLOR_Lab2LRGB),
  COLOR_Luv2LBGR(org.bytedeco.opencv.global.opencv_imgproc.COLOR_Luv2LBGR),
  COLOR_Luv2LRGB(org.bytedeco.opencv.global.opencv_imgproc.COLOR_Luv2LRGB),
  COLOR_BGR2YUV(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR2YUV),
  COLOR_RGB2YUV(org.bytedeco.opencv.global.opencv_imgproc.COLOR_RGB2YUV),
  COLOR_YUV2BGR(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2BGR),
  COLOR_YUV2RGB(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2RGB),
  COLOR_YUV2RGB_NV12(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2RGB_NV12),
  COLOR_YUV2BGR_NV12(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2BGR_NV12),
  COLOR_YUV2RGB_NV21(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2RGB_NV21),
  COLOR_YUV2BGR_NV21(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2BGR_NV21),
  COLOR_YUV420sp2RGB(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV420sp2RGB),
  COLOR_YUV420sp2BGR(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV420sp2BGR),
  COLOR_YUV2RGBA_NV12(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2RGBA_NV12),
  COLOR_YUV2BGRA_NV12(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2BGRA_NV12),
  COLOR_YUV2RGBA_NV21(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2RGBA_NV21),
  COLOR_YUV2BGRA_NV21(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2BGRA_NV21),
  COLOR_YUV420sp2RGBA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV420sp2RGBA),
  COLOR_YUV420sp2BGRA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV420sp2BGRA),
  COLOR_YUV2RGB_YV12(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2RGB_YV12),
  COLOR_YUV2BGR_YV12(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2BGR_YV12),
  COLOR_YUV2RGB_IYUV(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2RGB_IYUV),
  COLOR_YUV2BGR_IYUV(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2BGR_IYUV),
  COLOR_YUV2RGB_I420(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2RGB_I420),
  COLOR_YUV2BGR_I420(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2BGR_I420),
  COLOR_YUV420p2RGB(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV420p2RGB),
  COLOR_YUV420p2BGR(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV420p2BGR),
  COLOR_YUV2RGBA_YV12(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2RGBA_YV12),
  COLOR_YUV2BGRA_YV12(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2BGRA_YV12),
  COLOR_YUV2RGBA_IYUV(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2RGBA_IYUV),
  COLOR_YUV2BGRA_IYUV(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2BGRA_IYUV),
  COLOR_YUV2RGBA_I420(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2RGBA_I420),
  COLOR_YUV2BGRA_I420(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2BGRA_I420),
  COLOR_YUV420p2RGBA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV420p2RGBA),
  COLOR_YUV420p2BGRA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV420p2BGRA),
  COLOR_YUV2GRAY_420(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2GRAY_420),
  COLOR_YUV2GRAY_NV21(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2GRAY_NV21),
  COLOR_YUV2GRAY_NV12(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2GRAY_NV12),
  COLOR_YUV2GRAY_YV12(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2GRAY_YV12),
  COLOR_YUV2GRAY_IYUV(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2GRAY_IYUV),
  COLOR_YUV2GRAY_I420(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2GRAY_I420),
  COLOR_YUV420sp2GRAY(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV420sp2GRAY),
  COLOR_YUV420p2GRAY(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV420p2GRAY),
  COLOR_YUV2RGB_UYVY(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2RGB_UYVY),
  COLOR_YUV2BGR_UYVY(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2BGR_UYVY),
  COLOR_YUV2RGB_Y422(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2RGB_Y422),
  COLOR_YUV2BGR_Y422(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2BGR_Y422),
  COLOR_YUV2RGB_UYNV(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2RGB_UYNV),
  COLOR_YUV2BGR_UYNV(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2BGR_UYNV),
  COLOR_YUV2RGBA_UYVY(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2RGBA_UYVY),
  COLOR_YUV2BGRA_UYVY(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2BGRA_UYVY),
  COLOR_YUV2RGBA_Y422(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2RGBA_Y422),
  COLOR_YUV2BGRA_Y422(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2BGRA_Y422),
  COLOR_YUV2RGBA_UYNV(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2RGBA_UYNV),
  COLOR_YUV2BGRA_UYNV(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2BGRA_UYNV),
  COLOR_YUV2RGB_YUY2(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2RGB_YUY2),
  COLOR_YUV2BGR_YUY2(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2BGR_YUY2),
  COLOR_YUV2RGB_YVYU(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2RGB_YVYU),
  COLOR_YUV2BGR_YVYU(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2BGR_YVYU),
  COLOR_YUV2RGB_YUYV(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2RGB_YUYV),
  COLOR_YUV2BGR_YUYV(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2BGR_YUYV),
  COLOR_YUV2RGB_YUNV(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2RGB_YUNV),
  COLOR_YUV2BGR_YUNV(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2BGR_YUNV),
  COLOR_YUV2RGBA_YUY2(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2RGBA_YUY2),
  COLOR_YUV2BGRA_YUY2(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2BGRA_YUY2),
  COLOR_YUV2RGBA_YVYU(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2RGBA_YVYU),
  COLOR_YUV2BGRA_YVYU(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2BGRA_YVYU),
  COLOR_YUV2RGBA_YUYV(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2RGBA_YUYV),
  COLOR_YUV2BGRA_YUYV(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2BGRA_YUYV),
  COLOR_YUV2RGBA_YUNV(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2RGBA_YUNV),
  COLOR_YUV2BGRA_YUNV(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2BGRA_YUNV),
  COLOR_YUV2GRAY_UYVY(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2GRAY_UYVY),
  COLOR_YUV2GRAY_YUY2(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2GRAY_YUY2),
  COLOR_YUV2GRAY_Y422(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2GRAY_Y422),
  COLOR_YUV2GRAY_UYNV(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2GRAY_UYNV),
  COLOR_YUV2GRAY_YVYU(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2GRAY_YVYU),
  COLOR_YUV2GRAY_YUYV(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2GRAY_YUYV),
  COLOR_YUV2GRAY_YUNV(org.bytedeco.opencv.global.opencv_imgproc.COLOR_YUV2GRAY_YUNV),
  COLOR_RGBA2mRGBA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_RGBA2mRGBA),
  COLOR_mRGBA2RGBA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_mRGBA2RGBA),
  COLOR_RGB2YUV_I420(org.bytedeco.opencv.global.opencv_imgproc.COLOR_RGB2YUV_I420),
  COLOR_BGR2YUV_I420(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR2YUV_I420),
  COLOR_RGB2YUV_IYUV(org.bytedeco.opencv.global.opencv_imgproc.COLOR_RGB2YUV_IYUV),
  COLOR_BGR2YUV_IYUV(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR2YUV_IYUV),
  COLOR_RGBA2YUV_I420(org.bytedeco.opencv.global.opencv_imgproc.COLOR_RGBA2YUV_I420),
  COLOR_BGRA2YUV_I420(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGRA2YUV_I420),
  COLOR_RGBA2YUV_IYUV(org.bytedeco.opencv.global.opencv_imgproc.COLOR_RGBA2YUV_IYUV),
  COLOR_BGRA2YUV_IYUV(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGRA2YUV_IYUV),
  COLOR_RGB2YUV_YV12(org.bytedeco.opencv.global.opencv_imgproc.COLOR_RGB2YUV_YV12),
  COLOR_BGR2YUV_YV12(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR2YUV_YV12),
  COLOR_RGBA2YUV_YV12(org.bytedeco.opencv.global.opencv_imgproc.COLOR_RGBA2YUV_YV12),
  COLOR_BGRA2YUV_YV12(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGRA2YUV_YV12),
  COLOR_BayerBG2BGR(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerBG2BGR),
  COLOR_BayerGB2BGR(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerGB2BGR),
  COLOR_BayerRG2BGR(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerRG2BGR),
  COLOR_BayerGR2BGR(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerGR2BGR),
  COLOR_BayerRGGB2BGR(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerRGGB2BGR),
  COLOR_BayerGRBG2BGR(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerGRBG2BGR),
  COLOR_BayerBGGR2BGR(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerBGGR2BGR),
  COLOR_BayerGBRG2BGR(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerGBRG2BGR),
  COLOR_BayerRGGB2RGB(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerRGGB2RGB),
  COLOR_BayerGRBG2RGB(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerGRBG2RGB),
  COLOR_BayerBGGR2RGB(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerBGGR2RGB),
  COLOR_BayerGBRG2RGB(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerGBRG2RGB),
  COLOR_BayerBG2RGB(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerBG2RGB),
  COLOR_BayerGB2RGB(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerGB2RGB),
  COLOR_BayerRG2RGB(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerRG2RGB),
  COLOR_BayerGR2RGB(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerGR2RGB),
  COLOR_BayerBG2GRAY(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerBG2GRAY),
  COLOR_BayerGB2GRAY(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerGB2GRAY),
  COLOR_BayerRG2GRAY(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerRG2GRAY),
  COLOR_BayerGR2GRAY(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerGR2GRAY),
  COLOR_BayerRGGB2GRAY(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerRGGB2GRAY),
  COLOR_BayerGRBG2GRAY(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerGRBG2GRAY),
  COLOR_BayerBGGR2GRAY(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerBGGR2GRAY),
  COLOR_BayerGBRG2GRAY(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerGBRG2GRAY),
  COLOR_BayerBG2BGR_VNG(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerBG2BGR_VNG),
  COLOR_BayerGB2BGR_VNG(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerGB2BGR_VNG),
  COLOR_BayerRG2BGR_VNG(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerRG2BGR_VNG),
  COLOR_BayerGR2BGR_VNG(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerGR2BGR_VNG),
  COLOR_BayerRGGB2BGR_VNG(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerRGGB2BGR_VNG),
  COLOR_BayerGRBG2BGR_VNG(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerGRBG2BGR_VNG),
  COLOR_BayerBGGR2BGR_VNG(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerBGGR2BGR_VNG),
  COLOR_BayerGBRG2BGR_VNG(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerGBRG2BGR_VNG),
  COLOR_BayerRGGB2RGB_VNG(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerRGGB2RGB_VNG),
  COLOR_BayerGRBG2RGB_VNG(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerGRBG2RGB_VNG),
  COLOR_BayerBGGR2RGB_VNG(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerBGGR2RGB_VNG),
  COLOR_BayerGBRG2RGB_VNG(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerGBRG2RGB_VNG),
  COLOR_BayerBG2RGB_VNG(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerBG2RGB_VNG),
  COLOR_BayerGB2RGB_VNG(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerGB2RGB_VNG),
  COLOR_BayerRG2RGB_VNG(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerRG2RGB_VNG),
  COLOR_BayerGR2RGB_VNG(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerGR2RGB_VNG),
  COLOR_BayerBG2BGR_EA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerBG2BGR_EA),
  COLOR_BayerGB2BGR_EA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerGB2BGR_EA),
  COLOR_BayerRG2BGR_EA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerRG2BGR_EA),
  COLOR_BayerGR2BGR_EA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerGR2BGR_EA),
  COLOR_BayerRGGB2BGR_EA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerRGGB2BGR_EA),
  COLOR_BayerGRBG2BGR_EA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerGRBG2BGR_EA),
  COLOR_BayerBGGR2BGR_EA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerBGGR2BGR_EA),
  COLOR_BayerGBRG2BGR_EA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerGBRG2BGR_EA),
  COLOR_BayerRGGB2RGB_EA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerRGGB2RGB_EA),
  COLOR_BayerGRBG2RGB_EA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerGRBG2RGB_EA),
  COLOR_BayerBGGR2RGB_EA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerBGGR2RGB_EA),
  COLOR_BayerGBRG2RGB_EA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerGBRG2RGB_EA),
  COLOR_BayerBG2RGB_EA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerBG2RGB_EA),
  COLOR_BayerGB2RGB_EA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerGB2RGB_EA),
  COLOR_BayerRG2RGB_EA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerRG2RGB_EA),
  COLOR_BayerGR2RGB_EA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerGR2RGB_EA),
  COLOR_BayerBG2BGRA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerBG2BGRA),
  COLOR_BayerGB2BGRA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerGB2BGRA),
  COLOR_BayerRG2BGRA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerRG2BGRA),
  COLOR_BayerGR2BGRA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerGR2BGRA),
  COLOR_BayerRGGB2BGRA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerRGGB2BGRA),
  COLOR_BayerGRBG2BGRA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerGRBG2BGRA),
  COLOR_BayerBGGR2BGRA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerBGGR2BGRA),
  COLOR_BayerGBRG2BGRA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerGBRG2BGRA),
  COLOR_BayerRGGB2RGBA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerRGGB2RGBA),
  COLOR_BayerGRBG2RGBA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerGRBG2RGBA),
  COLOR_BayerBGGR2RGBA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerBGGR2RGBA),
  COLOR_BayerGBRG2RGBA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerGBRG2RGBA),
  COLOR_BayerBG2RGBA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerBG2RGBA),
  COLOR_BayerGB2RGBA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerGB2RGBA),
  COLOR_BayerRG2RGBA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerRG2RGBA),
  COLOR_BayerGR2RGBA(org.bytedeco.opencv.global.opencv_imgproc.COLOR_BayerGR2RGBA),
  COLOR_COLORCVT_MAX(org.bytedeco.opencv.global.opencv_imgproc.COLOR_COLORCVT_MAX);

  /** the OpenCV code. */
  private int m_Code;

  /**
   * Initializes the enum value.
   * 
   * @param code	the OpenCV code
   */
  private ColorConversionCode(int code) {
    m_Code = code;
  }

  /**
   * Returns the OpenCV code.
   * 
   * @return		the OpenCV code
   */
  public int getCode() {
    return m_Code;
  }
}
