import { Navigate, Route, Routes } from 'react-router-dom'
import Shell from './components/Shell'
import Dashboard from './pages/Dashboard'
import Proof from './pages/Proof'
import BuildLog from './pages/BuildLog'
import Skills from './pages/Skills'
import Impact from './pages/Impact'
import About from './pages/About'
import Admin from './pages/Admin'

export default function App(){
  return <Routes>
    <Route element={<Shell/>}>
      <Route index element={<Dashboard/>}/>
      <Route path="proof" element={<Proof/>}/>
      <Route path="proof/:slug" element={<Proof/>}/>
      <Route path="log" element={<BuildLog/>}/>
      <Route path="skills" element={<Skills/>}/>
      <Route path="impact" element={<Impact/>}/>
      <Route path="about" element={<About/>}/>
      <Route path="admin" element={<Admin/>}/>
      <Route path="*" element={<Navigate to="/" replace/>}/>
    </Route>
  </Routes>
}
