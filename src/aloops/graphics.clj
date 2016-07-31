(ns aloops.graphics
  (:require [quil.core :as q]
            [aloops.loopsapi :as loopsapi]
            [aloops.oscapi :as oscapi]))

(declare mundi)
(declare splash)

(defn setup-graphics []
  (q/color-mode :hsb 360 100 100 100)
  (q/text-align :left :center)
  )

(defn load-resources []
  (intern 'aloops.graphics 'mundi (q/load-image "resources/1_BDatos/mundi_1728x1080.jpg"))
  (intern 'aloops.graphics 'splash (q/load-image "resources/1_BDatos/splash_1280x800.png")))
;; Parece que definir una variable def dentro de defn está muy mal.
;; he buscado una alternativa: declare + intern
;; supongo que también podría haber usado alter-var-root, pero no sé qué es mejor
;; Importante!!! No olvidar que antes de pasar a uberjar tengo que quitar resources/ del path.

(defn adapt-to-frame [state]
  (let [ratio (/ (q/width) (q/height))
        img-sz (:img-sz state)]
    (if (>= ratio 1.6)
      (assoc state :img-sz (assoc img-sz
                             :ix (/ (- (q/width) (* (q/height) 1.6)) 2)
                             :iy 0
                             :img-width (* (q/height) 1.6)
                             :img-height (q/height)))
      (assoc state :img-sz (assoc img-sz
                             :ix 0
                             :iy (/ (- (q/height) (/ (q/width) 1.6)) 2)
                             :img-width (q/width)
                             :img-height (/ (q/width) 1.6))))))

(defn draw-background [state]
  (let [img-sz (:img-sz state)
        ix (:ix img-sz)
        iy (:iy img-sz)
        iwidth (:img-width img-sz)
        iheight (:img-height img-sz)]

    ;; background color = image background color
    (q/background 0 0 17)

    ;; image
    (q/image mundi ix iy iwidth iheight)

    ;; Grey strip behind album covers
    (q/no-stroke)
    (q/fill 50) ;; gris oscuro, fondo de las carátulas.
    (q/rect 0 0 (q/width) (+ (/ iheight 5) (/ (- (q/height) iheight) 2))) ;;rectángulo donde van las portadas

    ;; Credits
    (q/text-size (* iwidth 0.01))
    (q/fill 250)
    (q/text "www.antropoloops.com" (+ ix (* iwidth 0.015)) (+ iy (* iheight 0.95)))
    (q/fill 150)
    (q/text "www.mi-mina.com" (+ ix (* iwidth 0.015)) (+ iy (* iheight 0.97)))))

;; En el caso de las funciones que se ejecutan dentro de draw no tenemos que preocuparnos de
;; devolver el estado porque no es tenido en cuenta para nada.

(defn draw-splash-screen [state]
  (if @loopsapi/ready?
    nil
    (let [img-sz (:img-sz state)]
      (q/image splash (:ix img-sz) (:iy img-sz) (:img-width img-sz) (:img-height img-sz)))))

;; Graphic elements

(defn abanica [d h s b]
  (q/fill h s b)
  (q/ellipse 0 0 d d))

(defn draw-abanica-in-place [state loop-index]
  (let [info (loop-index @oscapi/loops-info)]
    (q/push-matrix)
    (q/translate (:x info) (:y info))
    ;TODO (q/rotate )
    (abanica 50 (:color-h info)(:color-s info)(:color-b info))
    (q/pop-matrix)))










