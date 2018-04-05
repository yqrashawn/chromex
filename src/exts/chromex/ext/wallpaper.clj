(ns chromex.ext.wallpaper
  "Use the chrome.wallpaper API to change the ChromeOS wallpaper.

     * available since Chrome 31
     * https://developer.chrome.com/extensions/wallpaper"

  (:refer-clojure :only [defmacro defn apply declare meta let partial])
  (:require [chromex.wrapgen :refer [gen-wrap-helper]]
            [chromex.callgen :refer [gen-call-helper gen-tap-all-events-call]]))

(declare api-table)
(declare gen-call)

; -- functions --------------------------------------------------------------------------------------------------------------

(defmacro set-wallpaper
  "Sets wallpaper to the image at url or wallpaperData with the specified layout

     |details| - https://developer.chrome.com/extensions/wallpaper#property-setWallpaper-details.

   This function returns a core.async channel of type `promise-chan` which eventually receives a result value.
   Signature of the result value put on the channel is [thumbnail] where:

     |thumbnail| - The jpeg encoded wallpaper thumbnail. It is generated by resizing the wallpaper to 128x60.

   In case of an error the channel closes without receiving any value and a relevant error object can be obtained via
   chromex.error/get-last-error.

   https://developer.chrome.com/extensions/wallpaper#method-setWallpaper."
  ([details] (gen-call :function ::set-wallpaper &form details)))

; -- convenience ------------------------------------------------------------------------------------------------------------

(defmacro tap-all-events
  "Taps all valid non-deprecated events in chromex.ext.wallpaper namespace."
  [chan]
  (gen-tap-all-events-call api-table (meta &form) chan))

; ---------------------------------------------------------------------------------------------------------------------------
; -- API TABLE --------------------------------------------------------------------------------------------------------------
; ---------------------------------------------------------------------------------------------------------------------------

(def api-table
  {:namespace "chrome.wallpaper",
   :since "31",
   :functions
   [{:id ::set-wallpaper,
     :name "setWallpaper",
     :since "33",
     :callback? true,
     :params
     [{:name "details", :type "object"}
      {:name "callback",
       :type :callback,
       :callback {:params [{:name "thumbnail", :optional? true, :type "binary"}]}}]}]})

; -- helpers ----------------------------------------------------------------------------------------------------------------

; code generation for native API wrapper
(defmacro gen-wrap [kind item-id config & args]
  (apply gen-wrap-helper api-table kind item-id config args))

; code generation for API call-site
(def gen-call (partial gen-call-helper api-table))