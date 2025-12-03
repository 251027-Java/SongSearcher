import AppRoutes from './AppRoutes'
import AppBar from './Components/AppBar'

function App() {

  return (
    <div className="bg-grey-200 min-h-screen min-w-full overflow-hidden">
      <AppBar/>
      <AppRoutes />
    </div>
  )
}

export default App
