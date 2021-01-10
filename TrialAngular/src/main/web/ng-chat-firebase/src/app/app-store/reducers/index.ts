import {
  Action,
  ActionReducer,
  ActionReducerMap,
  createFeatureSelector,
  createSelector,
  MetaReducer
} from '@ngrx/store';
import { environment } from '../../../environments/environment';
import * as fromSession from './session.reducer';


export interface State {

  [fromSession.sessionFeatureKey]: fromSession.State;
}

export const reducers: ActionReducerMap<State> = {

  [fromSession.sessionFeatureKey]: fromSession.reducer,
};

export function logger(reducer: ActionReducer<State>) { // 追加
  return (state: State | undefined, action: Action) => {
    const newState = reducer(state, action);
    console.log('action', action);
    console.log('state', newState);
    return newState;
  };
}

export const metaReducers: MetaReducer<State>[] = !environment.production ? [logger] : [];

export const selectSession = (state: State) => state.session;
export const getLoading = createSelector(selectSession, fromSession.getSessionLoading);
export const getSession = createSelector(selectSession, fromSession.getSessionData);