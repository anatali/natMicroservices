const config = {
    floor: {
        size: { x: 34, y: 30 }
    },
    player: {
         position: { x: 0.065, y: 0.075 },		 
         speed: 0.2
    },
    sonars: [
         {
            name: "sonar2",
            position: { x: 0.94, y: 0.88},
            senseAxis: { x: true, y: false }
        }
     ],
    movingObstacles: [
    ],
    staticObstacles: [
        {
        name: "wallUp",
        centerPosition: { x: 0.5, y: 1},
        size: { x: 1, y: 0.01}
        },
         {
            name: "wallDown",
            centerPosition: { x: 0.5, y: 0},
            size: { x: 1, y: 0.01}
        },
       {
            name: "wallLeft",
            centerPosition: { x: 0, y: 0.5},
            size: { x: 0.01, y: 1}
        },
        {
            name: "wallRight",
            centerPosition: { x: 1, y: 0.5},
            size: { x: 0.01, y: 1}
        },
        {
            name: "obstacle",
            centerPosition: {x: 0.45, y: 0.49},
            size: { x: 0.01, y: 0.57}
        }
    ]
}

export default config;
