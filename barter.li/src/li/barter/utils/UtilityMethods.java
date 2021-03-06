/*******************************************************************************
 * Copyright 2014, barter.li
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package li.barter.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;

/**
 * @author Vinay S Shenoy Utility methods for barter.li
 */
public class UtilityMethods {

	/**
	 * This method returns whether the device is connected to a network
	 * 
	 * @return <code>true</code> if connected, <code>false</code> otherwise
	 */
	public static boolean isNetworkConnected(final Context context) {
		final ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo activeNetwork = connManager.getActiveNetworkInfo();
		if ((activeNetwork != null) && activeNetwork.isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Generate a blurred Bitmap from an input Bitmap
	 * 
	 * @param context
	 * @param input
	 *            The bitmap to be blurred
	 * @param blurRadius
	 *            The blur radius, between 1 & 25, inclusive
	 * @return The blurred Bitmap
	 */
	public static Bitmap blurImage(Context context, Bitmap input, int blurRadius) {
		RenderScript rsScript = RenderScript.create(context);
		Allocation alloc = Allocation.createFromBitmap(rsScript, input);

		ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rsScript,
				alloc.getElement());
		blur.setRadius(blurRadius);
		blur.setInput(alloc);

		Bitmap result = Bitmap.createBitmap(input.getWidth(),
				input.getHeight(), input.getConfig());
		Allocation outAlloc = Allocation.createFromBitmap(rsScript, result);
		blur.forEach(outAlloc);
		outAlloc.copyTo(result);

		rsScript.destroy();
		return result;
	}
}
