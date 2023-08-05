import { BrowserRouter, Route, Routes } from "react-router-dom";
import Home from "./component/Home";

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
                </Routes>
            </BrowserRouter>
        </div>
    );
}
