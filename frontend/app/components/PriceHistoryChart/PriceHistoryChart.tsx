"use client"

import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend,
    TimeScale,
} from "chart.js"
import "chartjs-adapter-date-fns"
import {Line} from "react-chartjs-2";
import {SourceTypes} from "@/app/_types/source.types";

// Register the components Chart.js needs
ChartJS.register(
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend,
    TimeScale // Register the TimeScale
)

// Define props for the component
interface PriceHistoryChartProps {
    sources: SourceTypes[]
}

export default function PriceHistoryChart({ sources }: PriceHistoryChartProps) {
    // 1. Prepare the data for the chart
    const data = {
        datasets: sources.map((source) => {
            // Create a random color for each line for visual distinction
            const color = `rgba(${Math.floor(Math.random() * 255)}, ${Math.floor(
                Math.random() * 255
            )}, ${Math.floor(Math.random() * 255)}, 1)`

            return {
                label: new URL(source.url).hostname.replace("www.", ""),
                data: source.priceHistory.map((history) => ({
                    x: new Date(history.scrapedAt).getTime(), // Use timestamp for the x-axis
                    y: parseFloat(history.price), // Use price for the y-axis
                })),
                borderColor: color,
                backgroundColor: color,
                tension: 0.2, // Makes the line slightly curved
            }
        }),
    }

    // 2. Configure the chart's options
    const options = {
        responsive: true,
        plugins: {
            legend: {
                position: "top" as const,
                labels: {
                    // --- Change legend text color ---
                    color: '#f1f5f9', // slate-100 from your theme
                }
            },
            title: {
                display: true,
                text: "Price History Over Time",
                // --- Change title text color ---
                color: '#f1f5f9', // slate-100
                font: {
                    size: 18,
                }
            },
        },
        scales: {
            x: {
                type: "time" as const,
                time: {
                    unit: "day" as const,
                    tooltipFormat: "dd MMM yyyy",
                },
                title: {
                    display: true,
                    text: "Date",
                    // --- Change X-axis title color ---
                    color: '#94a3b8', // slate-400
                },
                ticks: {
                    // --- Change X-axis labels color ---
                    color: '#94a3b8', // slate-400
                },
                grid: {
                    // --- Change X-axis grid line color ---
                    color: '#334155', // slate-700
                }
            },
            y: {
                title: {
                    display: true,
                    text: "Price (RON)",
                    // --- Change Y-axis title color ---
                    color: '#94a3b8', // slate-400
                },
                ticks: {
                    // --- Change Y-axis labels color ---
                    color: '#94a3b8', // slate-400
                },
                grid: {
                    // --- Change Y-axis grid line color ---
                    color: '#334155', // slate-700
                }
            },
        },
    };

    return <Line options={options} data={data} />
}