import {BrowserRouter, Route, Routes} from "react-router-dom";
import Home from "./component/Home";
import Room from "./component/room/Room";
import {Provider} from "react-redux";
import {store} from "./redux/store";
import Game from "./component/room/Game";

/**
 * Root component.
 * @returns JSX contains all the components.
 */
export default function App() {
    return (
        <Provider store={store}>
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/room" element={<Room />} />
                    <Route path="/game/:gameId" element={<Game />} />
                </Routes>
            </BrowserRouter>
        </Provider>
    );
}
