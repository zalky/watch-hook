(ns build.client
  (:require ["echarts" :as chart]
            [reagent.core :as r]
            [reagent.dom :as dom]))

(defonce style-data
  ;; Style data available at run time. The actual definitions reside
  ;; in our SASS/CSS, our source of truth.
  (r/atom nil))

(defn config-style-data!
  "Retrieves CSS custom properties from the root node, and makes them
  available to the running application. The running application must
  reactively respond to changes in this style data."
  []
  (let [color (-> js/document
                  (.-documentElement)
                  (js/getComputedStyle)
                  (.getPropertyValue "--primary-color"))]
    (reset! style-data {:primary-color color})))

(defn watch-hook!
  "Ideally, this is run after shadow-cljs has updated the new CSS
  stylesheets in the browser."
  []
  (config-style-data!)
  ;; Maybe other things...
  )

(defn chart-config
  "Style data is recieved as props."
  [{color :primary-color}]
  {:xAxis  {:name "Type"
            :data ["shirt" "cardigan" "chiffon" "pants" "heels" "socks"]}
   :yAxis  {:name "Sales"}
   :series [{:name      "sales"
             :type      "bar"
             :data      [5 20 36 10 10 20]
             :itemStyle {:color color}}]}) ; Use style data here.

(defn update-chart!
  "Echarts requires style data to be provided as part of the data
  specification."
  [props chart]
  (->> (chart-config props)
       (clj->js)
       (.setOption chart)))

(defn create-chart!
  [props ref]
  (let [chart (->> "chart"
                   (.getElementById js/document)
                   (chart/init))]
    (update-chart! props chart)
    (reset! ref chart)))

(defn chart
  [_]
  (let [chart (r/atom nil)
        node  (r/atom nil)]
    (r/create-class
     {:component-did-mount
      (fn [this]
        (create-chart! (r/props this) chart))

      :component-did-update
      (fn [this]
        (update-chart! (r/props this) @chart))

      :reagent-render
      (fn [_]
        [:div {:id  "chart"
               :ref #(reset! node %)}])})))

(defn root
  []
  [:div
   [:input {:type     :button
            :value    "Simulate watch hook"
            :on-click #(watch-hook!)}]
   [chart @style-data]])

(defn render!
  []
  (watch-hook!)
  (some->> "container"
           (.getElementById js/document)
           (dom/render [root])))

(defn init!
  []
  (render!))

