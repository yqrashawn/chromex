(ns chromex.ext.idltest
  "An API to test IDL schema specifications.

     * available since Chrome 65
     * https://developer.chrome.com/extensions/idltest"

  (:refer-clojure :only [defmacro defn apply declare meta let partial])
  (:require [chromex.wrapgen :refer [gen-wrap-helper]]
            [chromex.callgen :refer [gen-call-helper gen-tap-all-events-call]]))

(declare api-table)
(declare gen-call)

; -- functions --------------------------------------------------------------------------------------------------------------

(defmacro send-array-buffer
  "Functions for testing binary data request/response parameters. The first two just return back the bytes they were passed in
   an array.

     |input| - https://developer.chrome.com/extensions/idltest#property-sendArrayBuffer-input.

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [array] where:

     |array| - https://developer.chrome.com/extensions/idltest#property-cb-array.

   In case of error the channel closes without receiving any result and relevant error object can be obtained via
   chromex.error/get-last-error.

   https://developer.chrome.com/extensions/idltest#method-sendArrayBuffer."
  ([input] (gen-call :function ::send-array-buffer &form input)))

(defmacro send-array-buffer-view
  "TODO(asargent) - we currently can't have [instanceOf=ArrayBufferView], I think because ArrayBufferView isn't an
   instantiable type. The best we might be able to do is have a 'choices' list including all the typed array subclasses like
   Uint8Array, Uint16Array, Float32Array, etc.

     |input| - https://developer.chrome.com/extensions/idltest#property-sendArrayBufferView-input.

   This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [array] where:

     |array| - https://developer.chrome.com/extensions/idltest#property-cb-array.

   In case of error the channel closes without receiving any result and relevant error object can be obtained via
   chromex.error/get-last-error.

   https://developer.chrome.com/extensions/idltest#method-sendArrayBufferView."
  ([input] (gen-call :function ::send-array-buffer-view &form input)))

(defmacro get-array-buffer
  "This function returns a core.async channel which eventually receives a result value and closes.
   Signature of the result value put on the channel is [buffer] where:

     |buffer| - https://developer.chrome.com/extensions/idltest#property-cb-buffer.

   In case of error the channel closes without receiving any result and relevant error object can be obtained via
   chromex.error/get-last-error.

   https://developer.chrome.com/extensions/idltest#method-getArrayBuffer."
  ([] (gen-call :function ::get-array-buffer &form)))

(defmacro nocompile-func
  "This function should not have C++ code autogenerated (the variable name |switch| should cause compile errors if it does).
   But the name should get defined and made visible from within extensions/apps code.

     |switch| - https://developer.chrome.com/extensions/idltest#property-nocompileFunc-switch.

   https://developer.chrome.com/extensions/idltest#method-nocompileFunc."
  ([switch] (gen-call :function ::nocompile-func &form switch)))

(defmacro nodefine-func
  "This function should not have C++ code autogenerated (the variable name |switch| should cause compile errors if it does).
   The name should also never be defined in Javascript and invisible to extensions/apps code.

     |switch| - https://developer.chrome.com/extensions/idltest#property-nodefineFunc-switch.

   https://developer.chrome.com/extensions/idltest#method-nodefineFunc."
  ([switch] (gen-call :function ::nodefine-func &form switch)))

; -- convenience ------------------------------------------------------------------------------------------------------------

(defmacro tap-all-events
  "Taps all valid non-deprecated events in chromex.ext.idltest namespace."
  [chan]
  (gen-tap-all-events-call api-table (meta &form) chan))

; ---------------------------------------------------------------------------------------------------------------------------
; -- API TABLE --------------------------------------------------------------------------------------------------------------
; ---------------------------------------------------------------------------------------------------------------------------

(def api-table
  {:namespace "chrome.idltest",
   :since "65",
   :functions
   [{:id ::send-array-buffer,
     :name "sendArrayBuffer",
     :callback? true,
     :params
     [{:name "input", :type "ArrayBuffer"}
      {:name "cb", :type :callback, :callback {:params [{:name "array", :type "[array-of-integers]"}]}}]}
    {:id ::send-array-buffer-view,
     :name "sendArrayBufferView",
     :callback? true,
     :params
     [{:name "input", :type "binary"}
      {:name "cb", :type :callback, :callback {:params [{:name "array", :type "[array-of-integers]"}]}}]}
    {:id ::get-array-buffer,
     :name "getArrayBuffer",
     :callback? true,
     :params [{:name "cb", :type :callback, :callback {:params [{:name "buffer", :type "ArrayBuffer"}]}}]}
    {:id ::nocompile-func, :name "nocompileFunc", :params [{:name "switch", :type "integer"}]}
    {:id ::nodefine-func, :name "nodefineFunc", :params [{:name "switch", :type "integer"}]}]})

; -- helpers ----------------------------------------------------------------------------------------------------------------

; code generation for native API wrapper
(defmacro gen-wrap [kind item-id config & args]
  (apply gen-wrap-helper api-table kind item-id config args))

; code generation for API call-site
(def gen-call (partial gen-call-helper api-table))