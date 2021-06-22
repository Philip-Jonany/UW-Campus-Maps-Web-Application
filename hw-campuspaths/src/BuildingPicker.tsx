//one textfield for each building selector
import React, {Component} from 'react';

interface BuildingPickerProps {
    value: string // building to display for a particular button
    buildings: any //list of valid buildings
    onChange(newB: string): void; // called when the one of the buildings is changed
}

class BuildingPicker extends Component<BuildingPickerProps> {
    onInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        let newB = event.target.value;
        this.props.onChange(newB);
    }

    render() {
        return (
            <div id="building-picker">
                <label>
                    <input
                        value={this.props.value}
                        onChange={this.onInputChange}
                    />
                </label>
            </div>
        );
    }
}

export default BuildingPicker;