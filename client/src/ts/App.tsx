import { BrowserRouter, Route, Routes } from "react-router-dom";
import Home from "./component/Home";
import Room from "./component/Room";

/**
 * Root component.
 * @returns JSX contains all the components.
 */
export default function App() {
    return (
        <div className="App">
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/room" element={<Room />} />
                </Routes>
            </BrowserRouter>
        </div>
    );
}
