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
import MapView from "./MapView";
import BuildingPicker from "./BuildingPicker";

interface AppState {
    buildings: Map<string,string> // map of short names to llonge names
    building1: string
    building2: string
    path: any[] //list of segments
}
class App extends Component<{}, AppState> {
    constructor(props: any) {
        super(props);

        this.state = {
            buildings: new Map<string,string>(), // a map of buildings
            building1: "",
            building2: "",
            path: []   //path is a list of segments of points
        }

        fetch("http://localhost:4567/listBuildings")
            .then(response => {
                if (!response.ok) {
                    throw new Error("The server could not handle the request");
                }
                return response.json();
            })
            .then(json => {
                let buildings = new Map<string,string>();
                for (var value in json) {
                    buildings.set(json[value], value);
                }
                this.setState({
                    buildings: buildings
                });
            })
            .catch(error => {
                alert('There has been a problem with listing buildings:');
            });

    }
    //called when building text fields are chang ed
    updateBuilding1 = (newB: string) => {
        this.setState({
            building1: newB
        });
    }

    updateBuilding2 = (newB: string) => {
        this.setState({
            building2: newB
        });
    }

    // is called when find path button is pressed
    updatePath = () => {

        if (this.state.building1 == "" || this.state.building2 == "") {
            alert("One of your buildings is empty");
        }
        else {

            let url = "http://localhost:4567/getPath?start=" + this.state.building1 + "&end=" + this.state.building2;
            fetch(url)
                .then(response => {
                    return response.json();
                })
                .then(json => {
                    let jsonpath = json.path;
                    let spath = [];
                    for (var i in jsonpath) {
                        spath.push(jsonpath[i]);
                    }
                    // alert(path.length);
                    this.setState({
                        path: spath
                    });
                })
                .catch(error => {
                    alert("At least one of your buildings is invalid.\nPlease refer to the list of valid buildings!");
                    // alert(error);
                })
        }
    }

    //is called when reset button is pressed
    resetAll = () => {
        this.setState({
            building1: "",
            building2: "",
            path: []
        })
    }
    render() {
        let b = this.state.buildings
        let it = b.keys()
        let list = ""
        b.forEach((key: string, val: string) => {
            list = list + (key + " - " + val + "\n");
        });
        return (
            <div>
                <h1>UW CampusPaths Application</h1>
                <MapView building1={this.state.building1} building2={this.state.building2} path = {this.state.path}/>
                Building1: RED<BuildingPicker value={this.state.building1} buildings={this.state.buildings} onChange={this.updateBuilding1} />
                Building2: BLUE<BuildingPicker value={this.state.building2} buildings={this.state.buildings} onChange={this.updateBuilding2} />
                <button onClick={this.updatePath}> Find Path</button>
                <button onClick={this.resetAll}> Reset</button>
                <br/>
                <br/>List of valid building codes and what they represent:
                <br/>{list.split('\n').map(str => <p>{str}</p>)}
            </div>


        );
    }
}

export default App;
