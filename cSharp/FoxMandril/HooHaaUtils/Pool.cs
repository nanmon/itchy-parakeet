using System;
using System.Collections.Generic;

namespace HooHaaUtils
{
	public class Pool <T>
	{
		public delegate T Factory();
		private List<T> unused;
		Factory factory;

		public Pool (Factory factory, int capacity)
		{
			unused = new List<T> (capacity);
			this.factory = factory;
		}

		public void Recycle(T obj){
			if (unused.Count < unused.Capacity)
				unused.Add (obj);
		}

		public T CreateObject(){
			if (unused.Count == 0)
				return factory();
			T r = unused [0];
			unused.RemoveAt (0);
			return r;
		}
	}
}

