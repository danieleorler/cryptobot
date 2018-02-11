import React, { Component } from 'react';
import moment from 'moment';
import './App.css';
import {Chart} from "react-google-charts";

class App extends Component {

    constructor(props) {
        super(props);
        this.state = {
            options: {
                title: '',
                legend: 'none'
            },
            data: [],
            formatters: [
                {
                    type: 'NumberFormat',
                    column: 1,
                    fractionDigits: 8
                },
                {
                    type: 'NumberFormat',
                    column: 2,
                    fractionDigits: 8
                },
                {
                    type: 'NumberFormat',
                    column: 3,
                    fractionDigits: 8
                }
            ]
        };
    }

    componentDidMount() {
        setInterval(this.updateChart, 30000);
    }

    updateChart = () => {
        fetch('http://localhost:8080/v1/api/crypto/XRPBTC')
            .then(results => {
                return results.json();
            }).then(data => {
            const dataTable = [];
            console.log(data);
            let analysis = data.analysis;
            dataTable.push(["time", "EMA(24)", "EMA(6)", "price"]);
            let time = moment().subtract(24, "hours");
            for (let i = 0; i < analysis.ema24h.length; i++) {
                dataTable.push([time.toDate(), analysis.ema24h[i], analysis.ema6h[i], analysis.prices24[i]]);
                time.add(1, "minutes");
            }
            this.setState({data: dataTable, options: {title: data.symbol}})
        }).catch((error) => {
            console.error(error);
        });
    };

    render() {
        return (
          <div className="App">
              <Chart
                  chartType="LineChart"
                  data={this.state.data}
                  options={this.state.options}
                  width="100%"
                  height="600px"
                  legend_toggle
              />
          </div>
        );
    }
}

export default App;
