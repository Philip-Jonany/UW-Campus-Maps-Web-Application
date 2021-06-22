/*
 * Copyright (C) 2021 Kevin Zatloukal.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Spring Quarter 2021 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

import React, {Component} from 'react';
import "./MapView.css";
import * as Path from "path";

interface MapViewProps {
    building1: string
    building2: string
    path: any //path is a list of segments of points
}


interface MapViewState {
    backgroundImage: HTMLImageElement | null;
}

class MapView extends Component<MapViewProps, MapViewState> {

    // NOTE:
    // This component is a suggestion for you to use, if you would like to.
    // It has some skeleton code that helps set up some of the more difficult parts
    // of getting <canvas> elements to display nicely with large images.
    //
    // If you don't want to use this component, you're free to delete it.

    canvas: React.RefObject<HTMLCanvasElement>;

    constructor(props: MapViewProps) {
        super(props);
        this.state = {
            backgroundImage: null,
        };
        this.fetchAndSaveImage();
        this.canvas = React.createRef();
    }

    componentDidMount() {
        this.drawBackgroundImage();
    }

    componentDidUpdate() {
        this.drawBackgroundImage();
    }

    fetchAndSaveImage() {
        // Creates an Image object, and sets a callback function
        // for when the image is done loading (it might take a while).
        let background: HTMLImageElement = new Image();
        background.onload = () => {
            this.setState({
                backgroundImage: background
            });
        };
        // Once our callback is set up, we tell the image what file it should
        // load from. This also triggers the loading process.
        background.src = "./campus_map.jpg";
    }

    //draws the background image and also the edge from building1 to building2
    drawBackgroundImage() {
        let canvas = this.canvas.current;
        if (canvas === null) throw Error("Unable to draw, no canvas ref.");
        let ctx = canvas.getContext("2d");
        if (ctx === null) throw Error("Unable to draw, no valid graphics context.");
        //
        if (this.state.backgroundImage !== null) { // This means the image has been loaded.
            // Sets the internal "drawing space" of the canvas to have the correct size.
            // This helps the canvas not be blurry.
            canvas.width = this.state.backgroundImage.width;
            canvas.height = this.state.backgroundImage.height;
            ctx.drawImage(this.state.backgroundImage, 0, 0);


        //    draw segments on the map
            let segments = this.props.path;
            for (var i in segments) {
                this.drawSegment(ctx, segments[i]);
                this.drawCircle(ctx, [segments[i].end.x,segments[i].end.y])
            }
            if (segments.length > 0) {
                this.drawStart(ctx, [segments[0].start.x,segments[0].start.y]);
                this.drawEnd(ctx, [segments[segments.length - 1].end.x,segments[segments.length - 1].end.y]);
            }
        }
    }
    //draws a short line between two points
    drawSegment(ctx: CanvasRenderingContext2D, segment: any) {
        let start = segment.start;
        let end = segment.end;
        ctx.beginPath();
        ctx.strokeStyle = "green";
        ctx.lineWidth = 10;
        ctx.moveTo(start.x,start.y);
        ctx.lineTo(end.x,end.y);
        ctx.stroke();
    }
    //draws the starting vertex as red
    drawStart = (ctx: CanvasRenderingContext2D, coordinate: [number, number]) => {
        ctx.beginPath();
        ctx.arc(coordinate[0], coordinate[1], 15, 0, 2 * Math.PI);
        ctx.fillStyle = "red";
        ctx.fill();
    };

    drawEnd = (ctx: CanvasRenderingContext2D, coordinate: [number, number]) => {
        ctx.beginPath();
        ctx.arc(coordinate[0], coordinate[1], 15, 0, 2 * Math.PI);
        ctx.fillStyle = "blue";
        ctx.fill();
    };
    //draws points between segments
    drawCircle = (ctx: CanvasRenderingContext2D, coordinate: [number, number]) => {
        ctx.fillStyle = "black";
        const radius = 10;
        ctx.beginPath();
        ctx.arc(coordinate[0], coordinate[1], radius, 0, 2 * Math.PI);
        ctx.fill();
    };

    render() {
        return (
            <canvas ref={this.canvas}/>
        )
    }
}

export default MapView;
