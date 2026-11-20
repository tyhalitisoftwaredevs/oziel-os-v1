import { NavLink, Outlet } from 'react-router-dom'
import { LayoutDashboard, Hexagon, ScrollText, Radar, BarChart3, UserRound, ShieldCheck } from 'lucide-react'

const nav = [
  { to:'/', label:'Command', icon:LayoutDashboard },
  { to:'/proof', label:'Proof', icon:Hexagon },
  { to:'/log', label:'Build Log', icon:ScrollText },
  { to:'/skills', label:'Skills', icon:Radar },
  { to:'/impact', label:'Impact', icon:BarChart3 },
  { to:'/about', label:'About', icon:UserRound },
]
export default function Shell(){
  return <div className="app-shell">
    <aside className="sidebar">
      <NavLink to="/" className="brand"><span className="brand-mark">O.</span><span>OZIEL <b>OS</b></span></NavLink>
      <nav>{nav.map(({to,label,icon:Icon})=>(
        <NavLink key={to} to={to} end={to==='/'} className={({isActive})=>`nav-item ${isActive?'active':''}`}><Icon size={18}/><span>{label}</span></NavLink>
      ))}</nav>
      <NavLink to="/admin" className="admin-link"><ShieldCheck size={17}/> Admin</NavLink>
    </aside>
    <main className="main"><Outlet/><footer className="footer">Built by Oziel • OZIEL OS v1.0 • Java + Spring Boot • Pretoria, South Africa</footer></main>
    <nav className="mobile-nav">{nav.map(({to,label,icon:Icon})=>(
      <NavLink key={to} to={to} end={to==='/'} className={({isActive})=>isActive?'active':''}><Icon size={20}/><span>{label}</span></NavLink>
    ))}</nav>
  </div>
}
